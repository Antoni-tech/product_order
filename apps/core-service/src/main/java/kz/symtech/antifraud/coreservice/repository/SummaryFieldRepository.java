package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SummaryFieldRepository extends JpaRepository<SummaryField, Long> {
    List<SummaryField> findAllBySummaryDataVersionId(Long id);
    List<SummaryField> findAllByParentSummaryFieldId(Long parentId);
}
