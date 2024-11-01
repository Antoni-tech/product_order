package kz.symtech.antifraud.adminservice.repository;

import kz.symtech.antifraud.adminservice.entities.users.UserPasswordRestore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordRestoreRepository extends JpaRepository<UserPasswordRestore, Long> {
    UserPasswordRestore findFirstByCodeAndUsedIsFalse(String code);
}
