package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelConnectorOutputRepository  extends JpaRepository<ModelConnectorOutput, Long> {
    Optional<ModelConnectorOutput> findBySummaryDataVersionId(Long summaryDataVersionId);
    List<ModelConnectorOutput> findBySummaryDataVersionIdIn(List<Long> summaryDataVersionId);
    @Transactional
    void deleteBySummaryDataVersionId(Long summaryDataVersionId);
}
