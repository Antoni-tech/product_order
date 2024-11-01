package kz.symtech.antifraud.models.dto.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ModelStructResponseDTO implements Serializable {
    private Long summaryDataVersionId;
    private List<ComponentModel> components;
    private List<FieldRelationDTO> fieldRelations;
    private Map<Long, ModelRuleCacheDTO> ruleMap;
    private Map<Long, TransactionCounterDTO> transactionCounterDTOMap;
}
