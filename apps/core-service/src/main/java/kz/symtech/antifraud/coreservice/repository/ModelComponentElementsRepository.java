package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.models.ModelComponentElements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelComponentElementsRepository extends JpaRepository<ModelComponentElements, Long> {
    List<ModelComponentElements> findAllByModelStructComponent_ModelStructId_SummaryDataVersionId(Long modelStructId);
}
