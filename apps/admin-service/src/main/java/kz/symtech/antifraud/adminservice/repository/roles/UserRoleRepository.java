package kz.symtech.antifraud.adminservice.repository.roles;

import kz.symtech.antifraud.adminservice.entities.roles.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Данный интерфейс предоставляет абстракцию функциональности отправки
 * запросов к БД к таблице UserRole
 */
@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
}
