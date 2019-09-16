package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.PasswordResetToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PasswordResetTokenRepository extends PagingAndSortingRepository<PasswordResetToken, Long> {
    PasswordResetToken findByUserCodeAndToken(Long id, String token);
    @Query(value = "select * from password_reset_token where expiry_date between '2019-09-15' and ?1", nativeQuery = true)
    Page<PasswordResetToken> findByExpiryDate(String date, Pageable pageable);
}
