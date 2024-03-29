package br.org.catolicasc.surca.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "password_reset_token_seq", sequenceName = "password_reset_token_seq",
        initialValue = 2, allocationSize = 1)
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_token_seq")
    private Long code;

    private String token;

    @OneToOne(targetEntity = User.class)
    @NotNull
    private User user;

    private LocalDateTime expiryDate;
}
