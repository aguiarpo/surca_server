package br.org.catolicasc.surca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        String email = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null)
            email = auth.getName();
        String finalEmail = email;
        return () -> Optional.of(finalEmail);
    }
}
