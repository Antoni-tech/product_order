package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryFieldSubData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryFieldSubDataRepository extends JpaRepository<SummaryFieldSubData, Long> {
}
