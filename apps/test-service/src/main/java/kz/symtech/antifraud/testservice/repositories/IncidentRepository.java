package kz.symtech.antifraud.testservice.repositories;

import kz.symtech.antifraud.testservice.entities.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
}
