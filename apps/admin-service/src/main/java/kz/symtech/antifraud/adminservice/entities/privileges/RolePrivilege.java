package kz.symtech.antifraud.adminservice.entities.privileges;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.symtech.antifraud.adminservice.entities.roles.Roles;
import kz.symtech.antifraud.models.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Данный класс представляет собой абстракцию над таблицей ROLE_PRIVILEGE
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "role_privilege")
public class RolePrivilege extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Privileges privileges;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Roles role;
}
