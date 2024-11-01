package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.models.ModelStruct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModelStructRepository  extends JpaRepository<ModelStruct, Long> {
    Optional<ModelStruct> findBySummaryDataVersionId(Long summaryDataVersionId);
}
