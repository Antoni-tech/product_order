package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SummaryDataRepository extends JpaRepository<SummaryData, UUID> {
}
