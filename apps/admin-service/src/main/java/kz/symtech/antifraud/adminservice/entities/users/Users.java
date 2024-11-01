package kz.symtech.antifraud.adminservice.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import kz.symtech.antifraud.adminservice.entities.roles.UserRole;
import kz.symtech.antifraud.models.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * Данный класс представляет собой абстракцию над таблией USERS
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class Users extends BaseEntity {

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String email;

    private String name;

    private String phone;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Date lastLogin;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonIgnore
    private String salt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private Set<UserRole> userRoles;

}
