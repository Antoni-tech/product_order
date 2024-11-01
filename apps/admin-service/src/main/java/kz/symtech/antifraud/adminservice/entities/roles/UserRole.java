package kz.symtech.antifraud.adminservice.entities.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.symtech.antifraud.adminservice.entities.users.Users;
import kz.symtech.antifraud.models.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


/**
 * Данный класс представляет собой абстракцию над такблией USER_ROLE
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class UserRole extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Roles role;
}
