package br.org.catolicasc.surca.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq",
        initialValue = 2, allocationSize = 1)
public class User extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Getter @Setter private Long id;

    @NotNull
    @Getter @Setter private String name;

    @Email
    @Column(unique = true)
    @Getter @Setter private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8)
    @NotEmpty
    @Getter @Setter private String password;

    @Size(min = 2, max = 2)
    @NotEmpty
    @Getter @Setter private String state;
    @NotEmpty
    @Getter @Setter private String city;

    @NotNull
    @Size(max = 15)
    @Getter @Setter private String telephone1;
    @Size(max = 15)
    @Getter @Setter private String telephone2;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Getter @Setter private LevelsOfAccess levelsOfAccess;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Vet vet;

    public void setBcryptPassword(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.password = bCryptPasswordEncoder.encode(this.password);
    }
}
