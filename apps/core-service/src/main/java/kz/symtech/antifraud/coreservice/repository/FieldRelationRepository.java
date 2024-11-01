package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.others.FieldRelation;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRelationRepository extends JpaRepository<FieldRelation, Long> {
    FieldRelation findByModelStructIdAndVarSummaryFieldId(Long modelId, Long varSummaryFieldId);
    List<FieldRelation> findAllByModelStructSummaryDataVersionId(Long summaryDataVersionModelId);
    List<FieldRelation> findAllByModelStructId(Long summaryDataVersionModelId);
    List<FieldRelation> findAllByVarSummaryFieldIn(List<SummaryField> varSummaryFields);
    List<FieldRelation> findAllBySrcSummaryFieldIn(List<SummaryField> srcSummaryFields);
    int countByModelStructSummaryDataVersionId(Long sDVModelId);
}
