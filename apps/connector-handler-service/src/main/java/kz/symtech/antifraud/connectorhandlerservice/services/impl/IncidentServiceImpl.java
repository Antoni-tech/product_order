package kz.symtech.antifraud.connectorhandlerservice.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kz.symtech.antifraud.connectorhandlerservice.dto.IncidentListResponseDTO;
import kz.symtech.antifraud.connectorhandlerservice.entities.Incident;
import kz.symtech.antifraud.connectorhandlerservice.repositories.IncidentRepository;
import kz.symtech.antifraud.connectorhandlerservice.services.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final EntityManager entityManager;

    @Override
    public void save(Incident incident) {
        incidentRepository.save(incident);
    }

    @Override
    public IncidentListResponseDTO getAll(Long page, Long size, Long modelId, Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Incident> criteriaQuery = cb.createQuery(Incident.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        Root<Incident> root = criteriaQuery.from(Incident.class);
        Root<Incident> countRoot = countQuery.from(Incident.class);

        Predicate predicate = buildPredicate(cb, root, modelId, userId);
        Predicate countPredicate = buildPredicate(cb, countRoot, modelId, userId);

        criteriaQuery.where(predicate);
        criteriaQuery.orderBy(cb.desc(root.get("createdAt")));

        countQuery.select(cb.count(countRoot));
        countQuery.where(countPredicate);

        TypedQuery<Incident> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int) (page * size));
        typedQuery.setMaxResults(Math.toIntExact(size));

        List<Incident> resultList = typedQuery.getResultList();
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new IncidentListResponseDTO(totalCount, resultList);
    }

    private Predicate buildPredicate(CriteriaBuilder cb, Root<Incident> root, Long modelId, Long userId) {
        Predicate predicate = cb.conjunction();

        predicate = cb.and(predicate, cb.equal(root.get("summaryDataVersionModelId"), modelId));
        predicate = cb.and(predicate, cb.equal(root.get("userId"), userId));

        return predicate;
    }
}
