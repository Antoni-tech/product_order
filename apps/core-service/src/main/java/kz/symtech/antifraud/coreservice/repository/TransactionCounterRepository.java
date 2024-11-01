package kz.symtech.antifraud.coreservice.repository;

import jakarta.persistence.LockModeType;
import kz.symtech.antifraud.coreservice.entities.models.TransactionCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionCounterRepository extends JpaRepository<TransactionCounter, Long> {
    TransactionCounter findByModelStructSummaryDataVersionIdAndSummaryDataVersionId(Long summaryDataVersionModelId, Long summaryDataVersionId);
    List<TransactionCounter> findByModelStructSummaryDataVersionIdAndSummaryDataVersionIdIn(Long summaryDataVersionModelId, List<Long> summaryDataVersionIds);
    List<TransactionCounter> findAllByModelStructSummaryDataVersionId(Long summaryDataVersionModelId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TransactionCounter t " +
            "WHERE t.modelStruct.summaryDataVersion.id = :sDVModelId AND t.summaryDataVersion.id = :sDVComponentId")
    TransactionCounter findByIdWithWriteLock(@Param("sDVModelId") Long sDVModelId, @Param("sDVComponentId") Long sDVComponentId);
}
