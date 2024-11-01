package kz.symtech.antifraud.adminservice.repository.privileges;

import kz.symtech.antifraud.adminservice.entities.privileges.Privileges;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Данный интерфейс предоставляет абстракцию над
 * функциональностью запросов в БД к таблице PRIVILEGES
 */
@Repository
public interface PrivilegesRepository extends CrudRepository<Privileges, Long> {
    List<Privileges> findByIdIn(List<Long> privilegesId);
}
