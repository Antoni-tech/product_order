package kz.symtech.antifraud.connectorhandlerservice.utils;

import kz.symtech.antifraud.models.dto.model.FieldRelationDTO;
import kz.symtech.antifraud.models.dto.model.SummaryFieldCacheDTO;
import kz.symtech.antifraud.models.enums.SummaryFieldType;
import kz.symtech.antifraud.models.utils.ObjectMapperUtils;
import org.springframework.data.util.Pair;

import java.util.*;

public class TransactionDataUtil {

    public static void fillMapWithTransactionData(
            Map<String, Object> transaction,
            List<SummaryFieldCacheDTO> summaryFields,
            FieldRelationDTO fieldRelation,
            Map<String, Object> map) {

        Stack<Pair<String, SummaryFieldType>> keys = new Stack<>();
        fillStack(keys, summaryFields, fieldRelation.getSourceId());

        Object value;
        while (!keys.isEmpty()) {
            Pair<String, SummaryFieldType> p = keys.pop();
            String key = p.getFirst();
            SummaryFieldType type = p.getSecond();

            if (type.equals(SummaryFieldType.OBJECT)) {
                transaction = ObjectMapperUtils.fromObjectToMap(transaction.get(key));
            } else {
                value = transaction.get(fieldRelation.getFieldNameSrc());
                map.put(fieldRelation.getFieldNameVar(), value);
            }
        }
    }

    public static void fillStack(
            Stack<Pair<String, SummaryFieldType>> keys,
            List<SummaryFieldCacheDTO> summaryFields,
            Long sourceId) {

        SummaryFieldCacheDTO summaryFieldCacheDTO = summaryFields.stream()
                .filter(x -> x.getId().equals(sourceId))
                .findFirst()
                .orElse(null);

        if (Objects.nonNull(summaryFieldCacheDTO)) {
            keys.push(Pair.of(summaryFieldCacheDTO.getName(), summaryFieldCacheDTO.getType()));

            Long sourceParentId = Objects.isNull(summaryFieldCacheDTO.getParentSummaryField()) ? null :
                    summaryFieldCacheDTO.getParentSummaryField().getId();
            fillStack(keys, summaryFields, sourceParentId);
        }
    }

    public static void fillSummaryFields(Set<SummaryFieldCacheDTO> summaryFieldSet,
                                  Map<Long, SummaryFieldCacheDTO> summaryFieldMap,
                                  List<FieldRelationDTO> fieldRelations,
                                  Boolean isSource) {

        Set<Long> parentIds = new HashSet<>();
        List<SummaryFieldCacheDTO> filteredSummaryFields = new ArrayList<>();

        List<Long> ids = isSource
                ? fieldRelations.stream().map(FieldRelationDTO::getSourceId).toList()
                : fieldRelations.stream().map(FieldRelationDTO::getSummaryFieldId).toList();

        ids.forEach(id -> {
            SummaryFieldCacheDTO summaryFieldCacheDTO = summaryFieldMap.get(id);

            if (Objects.nonNull(summaryFieldCacheDTO)) {
                filteredSummaryFields.add(summaryFieldCacheDTO);
            }
        });

        filteredSummaryFields.forEach(s -> {
            summaryFieldSet.add(s);
            if (Objects.nonNull(s.getParentSummaryField())) {
                parentIds.add(s.getParentSummaryField().getId());
            }
        });

        addAllParents(summaryFieldSet, summaryFieldMap, parentIds);
    }

    private static void addAllParents(Set<SummaryFieldCacheDTO> summaryFieldSet,
                               Map<Long, SummaryFieldCacheDTO> summaryFieldMap,
                               Set<Long> ids) {

        Set<Long> newIds = new HashSet<>();

        List<SummaryFieldCacheDTO> filteredSummaryFields = new ArrayList<>();
        ids.forEach(id -> {
            SummaryFieldCacheDTO summaryFieldCacheDTO = summaryFieldMap.get(id);

            if (Objects.nonNull(summaryFieldCacheDTO)) {
                filteredSummaryFields.add(summaryFieldCacheDTO);
            }
        });

        filteredSummaryFields.forEach(s -> {
            summaryFieldSet.add(s);
            if (Objects.nonNull(s.getParentSummaryField())) {
                newIds.add(s.getParentSummaryField().getId());
            }
        });

        if (!ids.isEmpty()) {
            addAllParents(summaryFieldSet, summaryFieldMap, newIds);
        }
    }
}
