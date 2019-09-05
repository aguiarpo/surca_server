package br.org.catolicasc.surca.service;

import br.org.catolicasc.surca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        br.org.catolicasc.surca.model.User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_VETERINARIO", "ROLE_ADMIN");
        List<GrantedAuthority> authorityListVeterinario = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_VETERINARIO");
        List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
        List<GrantedAuthority> nivelUser;
        switch (user.getLevelsOfAccess()){
            case VETERINARIO:
                nivelUser = authorityListVeterinario;
                break;
            case ADMIN:
                nivelUser = authorityListAdmin;
                break;
            default:
                nivelUser = authorityListUser;
                break;
        }
        return new User(user.getEmail(), user.getPassword(),nivelUser);
    }
}
