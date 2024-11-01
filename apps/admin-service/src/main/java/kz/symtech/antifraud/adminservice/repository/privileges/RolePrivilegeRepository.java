package kz.symtech.antifraud.adminservice.repository.privileges;

import kz.symtech.antifraud.adminservice.entities.privileges.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Данный интерфейс предоставляет абстракцию над функциональностью
 * отправки запросов в БД к таблице RolePrivilege
 * <p>
 * Не содержит кастомных методов
 */
@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {

    List<RolePrivilege> findAllByRoleId(Long id);

}
