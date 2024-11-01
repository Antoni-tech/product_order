package kz.symtech.antifraud.adminservice.entities.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.symtech.antifraud.adminservice.entities.privileges.RolePrivilege;
import kz.symtech.antifraud.models.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

/**
 * Данный класс представляет собой абстракцию нат таблицей ROLES
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Roles extends BaseEntity {
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "role_id")
    private Set<RolePrivilege> rolePrivileges;
    @OneToMany
    @JoinColumn(name = "role_id")
    private Set<UserRole> userRoles;
    private String name;
    private Boolean defaultRole;
    private Date createdDate;
    private Date updateDate;
}
