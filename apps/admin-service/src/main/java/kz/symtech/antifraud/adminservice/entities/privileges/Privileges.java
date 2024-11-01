package kz.symtech.antifraud.adminservice.entities.privileges;

import kz.symtech.antifraud.models.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

/**
 * Данный класс спредставляет собой абстракцию над таблицей PRIVILEGES
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "privileges")
public class Privileges extends BaseEntity {
/*    @OneToMany
    @JoinColumn(name = "PRIVILEGES_ID")
    private Set<RolePrivilege> rolePrivileges;*/
    private String name;
    private String tag;
    private String privilegeGroup;
    private Date createdDate;
    private Date updateDate;
}
