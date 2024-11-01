package kz.symtech.antifraud.connectorhandlerservice.repositories;

import kz.symtech.antifraud.connectorhandlerservice.entities.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
}
