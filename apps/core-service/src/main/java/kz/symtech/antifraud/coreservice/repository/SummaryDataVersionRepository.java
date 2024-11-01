package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.enums.SummaryVersionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SummaryDataVersionRepository extends JpaRepository<SummaryDataVersion, Long> {
    List<SummaryDataVersion> findAllBySummaryDataIdAndState(UUID summaryDataId, SummaryVersionState state);
    Optional<SummaryDataVersion> findFirstBySummaryDataIdOrderByVersionDesc(UUID summaryDataId);
    Optional<SummaryDataVersion> findBySummaryDataIdAndIsActive(UUID summaryDataId, Boolean isActive);
    Optional<SummaryDataVersion> findBySummaryDataId(UUID summaryDataId);
    List<SummaryDataVersion> findAllBySummaryDataId(UUID summaryDataId);

}
