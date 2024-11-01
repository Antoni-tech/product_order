package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.models.ModelStructComponents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelStructComponentsRepository extends JpaRepository<ModelStructComponents, Long> {
    List<ModelStructComponents> findAllBySummaryDataVersionId(Long summaryDataVersionId);
    List<ModelStructComponents> findAllByModelStructId(Long modelStructId);
    List<ModelStructComponents> findAllByModelStruct_SummaryDataVersionId(Long modelStructId);
    ModelStructComponents findBySummaryDataVersionId(Long modelStructId);

    @Query("SELECT msc FROM ModelStructComponents msc WHERE msc.modelStruct.id = " +
            "(SELECT mscInner.modelStruct.id FROM ModelStructComponents mscInner WHERE mscInner.summaryDataVersion.id = :summaryDataVersionId)")
    List<ModelStructComponents> findAllComponentsBySummaryDataVersionId(@Param("summaryDataVersionId") Long summaryDataVersionId);

}
