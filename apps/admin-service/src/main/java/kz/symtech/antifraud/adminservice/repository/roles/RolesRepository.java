package kz.symtech.antifraud.adminservice.repository.roles;

import kz.symtech.antifraud.adminservice.entities.roles.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Данный интерфейс представляет абстракцию над функциональностью
 * отправки запросов в БД к таблице ROLES
 */
@Repository
public interface RolesRepository extends CrudRepository<Roles,Long> {
    Set<Roles> findByIdIn(List<Long> rolesIds);
}
