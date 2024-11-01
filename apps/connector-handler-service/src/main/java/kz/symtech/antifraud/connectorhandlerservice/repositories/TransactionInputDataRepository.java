package kz.symtech.antifraud.connectorhandlerservice.repositories;

import kz.symtech.antifraud.connectorhandlerservice.entities.TransactionInputData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionInputDataRepository extends JpaRepository<TransactionInputData, Long> {
    TransactionInputData findBySummaryDataVersionModelIdAndSummaryDataVersionConnectorId(Long sDVModelId, Long sDVConnectorId);
    TransactionInputData findBySummaryDataVersionModelIdAndSummaryDataVersionConnectorIdAndSummaryDataVersionRuleId(
            Long sDVModelId, Long sDVConnectorId, Long sDVRuleId);
}
