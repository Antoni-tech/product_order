package kz.symtech.antifraud.testservice.repositories;

import kz.symtech.antifraud.testservice.entities.TransactionData;
import kz.symtech.antifraud.testservice.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionDataRepository extends JpaRepository<TransactionData, Long> {
    List<TransactionData> findAllByState(State state);
    TransactionData findBySummaryDataModelIdAndSummaryDataConnectorId(UUID summaryDataModelId, UUID summaryDataConnectorId);
}
