package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.rules.QualityOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualityOperationRepository extends JpaRepository<QualityOperation, Long> {
    Optional<QualityOperation> findByRuleExpressionId(Long ruleExpressionId);

    List<QualityOperation> findAllByRuleExpressionSummaryDataVersionRuleId(Long versionRuleId);
    void deleteAllByRuleExpressionIdIn(List<Long> ruleIds);
}
