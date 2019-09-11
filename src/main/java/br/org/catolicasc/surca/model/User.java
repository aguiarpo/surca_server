package br.org.catolicasc.surca.model;

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
@Getter
@Setter
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq",
        initialValue = 2, allocationSize = 1)
@Table(indexes = {@Index(name = "index_name", columnList="name"),
        @Index(name = "index_levels_of_access", columnList="levelsOfAccess"),
        @Index(name = "index_city", columnList="city"),
        @Index(name = "index_state", columnList="state"),
        @Index(name = "index_created_by", columnList="createdBy"),
        @Index(name = "index_last_modified_by", columnList="lastModifiedBy")},
        uniqueConstraints = @UniqueConstraint(name = "email", columnNames=  "email" ))
public class User extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column(name = "id")
    private Long code;

    @NotNull
    private String name;

    @Email
    private String email;

    @Size(min = 8)
    @NotEmpty
    private String password;

    @Size(min = 2, max = 2)
    @NotEmpty
    private String state;
    @NotEmpty
    private String city;

    @NotNull
    @Size(max = 15)
    private String telephone1;
    @Size(max = 15)
    private String telephone2;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LevelsOfAccess levelsOfAccess = LevelsOfAccess.USUARIO;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status = Status.VISIBLE;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Vet vet;

    public void setBcryptPassword(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.password = bCryptPasswordEncoder.encode(this.password);
    }
}
