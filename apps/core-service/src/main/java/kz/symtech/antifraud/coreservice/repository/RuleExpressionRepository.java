package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.rules.RuleExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RuleExpressionRepository extends JpaRepository<RuleExpression, Long> {
    List<RuleExpression> findAllBySummaryDataVersionRuleId(Long sDVRuleId);
    void deleteAllBySummaryDataVersionRuleId(Long sDVRuleId);
    Optional<RuleExpression> findByIdAndSummaryDataVersionRuleId(Long id, Long sDVRuleId);
    void deleteAllBySummaryDataVersionRuleIdAndIdNotIn(Long sDVRuleId, List<Long> ids);
}
