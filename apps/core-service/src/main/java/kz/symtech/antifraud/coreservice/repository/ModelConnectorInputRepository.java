package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelConnectorInputRepository  extends JpaRepository<ModelConnectorInput, Long> {
    Optional<ModelConnectorInput> findBySummaryDataVersionId(Long summaryDataVersionId);

    List<ModelConnectorInput> findBySummaryDataVersionIdIn(List<Long> summaryDataVersionIds);
    @Transactional
    void deleteBySummaryDataVersionId(Long summaryDataVersionId);
}
