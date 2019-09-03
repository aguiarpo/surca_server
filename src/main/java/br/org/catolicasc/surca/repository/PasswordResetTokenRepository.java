package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
}
