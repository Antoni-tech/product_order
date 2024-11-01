package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.models.ModelRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRuleRepository  extends JpaRepository<ModelRule, Long> {
    Optional<ModelRule> findBySummaryDataVersionId(Long summaryDataVersionId);
    List<ModelRule> findBySummaryDataVersionIdIn(List<Long> summaryDataVersionIds);
    @Transactional
    void deleteBySummaryDataVersionId(Long summaryDataVersionId);
}
