package kz.symtech.antifraud.adminservice.repository.users;


import kz.symtech.antifraud.adminservice.entities.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Данный интефрейс предоставляет абстракцию над функционалом отправки
 * запросов к БД к таблице USERS
 */
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByLogin(String login);

    List<Users> findByEmail(String email);

    List<Users> findAllByIdIn(List<Long> ids);
}
