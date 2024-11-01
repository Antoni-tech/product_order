package kz.symtech.antifraud.coreservice.services.impl;

import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationDeleteRequestDTO;
import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationRequestDTO;
import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationRequestSubDataDTO;
import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationResponseDTO;
import kz.symtech.antifraud.coreservice.entities.models.ModelStruct;
import kz.symtech.antifraud.coreservice.entities.others.FieldRelation;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryField;
import kz.symtech.antifraud.coreservice.exceptions.IdenticalValueException;
import kz.symtech.antifraud.coreservice.exceptions.RelationEstablishException;
import kz.symtech.antifraud.coreservice.mapper.FieldRelationCacheMapper;
import kz.symtech.antifraud.coreservice.mapper.FieldRelationResponseMapper;
import kz.symtech.antifraud.coreservice.repository.FieldRelationRepository;
import kz.symtech.antifraud.coreservice.repository.ModelStructRepository;
import kz.symtech.antifraud.coreservice.services.FieldRelationService;
import kz.symtech.antifraud.coreservice.services.SummaryFieldService;
import kz.symtech.antifraud.models.dto.model.FieldRelationDTO;
import kz.symtech.antifraud.models.enums.SummaryFieldType;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FieldRelationServiceImpl implements FieldRelationService {

    private final FieldRelationRepository fieldRelationRepository;
    private final ModelStructRepository modelStructRepository;
    private final SummaryFieldService summaryFieldService;
    private final FieldRelationCacheMapper fieldRelationCacheMapper;
    private final FieldRelationResponseMapper fieldRelationResponseMapper;

    @Override
    @Transactional
    public Long saveOrUpdate(FieldRelationRequestDTO fieldRelationRequestDTO) {
        ModelStruct model = getModel(fieldRelationRequestDTO.getSDVModelStructId());

        List<FieldRelation> res = fieldRelationRequestDTO.getFieldRelationRequestSubDataDTOList()
                .stream()
                .map(subData -> handleSaveRequest(model, subData))
                .filter(Objects::nonNull)
                .toList();

        return (long) res.size();
    }

    public FieldRelation handleSaveRequest(ModelStruct modelStruct, FieldRelationRequestSubDataDTO f) {
        log.info("checking if ids are same");
        checkForSameIds(f.getVarSummaryFieldId(), f.getSrcSummaryFieldId());

        log.info("checking if same fieldRelation exists...");
        FieldRelation fieldRelation = updateIfExists(modelStruct.getId(), f.getVarSummaryFieldId());

        SummaryField varSummaryField = summaryFieldService.getEntity(f.getVarSummaryFieldId());
        SummaryField srcSummaryField = summaryFieldService.getEntity(f.getSrcSummaryFieldId());

        checkObjectType(srcSummaryField);
        checkObjectType(varSummaryField);

        if (Objects.isNull(fieldRelation)) {
            fieldRelation = new FieldRelation();
            fieldRelation.setModelStruct(modelStruct);
            fieldRelation.setVarSummaryField(varSummaryField);
        }
        fieldRelation.setSrcSummaryField(srcSummaryField);

        return fieldRelationRepository.save(fieldRelation);
    }

    private void checkObjectType(SummaryField summaryField) {
        if (Objects.nonNull(summaryField.getFieldType()) && summaryField.getFieldType().equals(SummaryFieldType.OBJECT)) {
            throw new RelationEstablishException(List.of(String.format("Field with id %s must not be 'OBJECT' type",
                    summaryField.getId())));
        }
    }

    @Override
    public List<FieldRelationDTO> getFieldRelations(Long summaryDataVersionModelId) {
        List<FieldRelation> fieldRelations = fieldRelationRepository.findAllByModelStructSummaryDataVersionId(summaryDataVersionModelId);
        return fieldRelations.stream().map(fieldRelationCacheMapper).toList();
    }

    @Override
    public List<FieldRelationResponseDTO> getAllBySDVModelId(Long summaryDataVersionModelId) {
        return fieldRelationRepository.findAllByModelStructSummaryDataVersionId(summaryDataVersionModelId)
                .stream()
                .map(fieldRelationResponseMapper)
                .toList();
    }

    private ModelStruct getModel(Long sDVModelId) {
        return modelStructRepository.findBySummaryDataVersionId(sDVModelId)
                .orElseThrow(() -> new NotFoundException("Model Struct not found"));
    }

    @Override
    public void delete(FieldRelationDeleteRequestDTO fieldRelationDeleteRequestDTO) {
        List<FieldRelation> fieldRelationsOfModel = fieldRelationRepository
                .findAllByModelStructSummaryDataVersionId(fieldRelationDeleteRequestDTO.getSDVModelId());
        List<FieldRelation> fieldRelationsToDelete;

        if (Objects.isNull(fieldRelationDeleteRequestDTO.getSDVRuleId())) {
            fieldRelationsToDelete =
                    filterFieldRelations(fieldRelationsOfModel, fieldRelationDeleteRequestDTO.getFieldRelationIds());
        } else {
            List<FieldRelation> fieldRelationsOfRule = fieldRelationsOfModel
                    .stream()
                    .filter(x -> x.getVarSummaryField().getSummaryDataVersion().getId().equals(fieldRelationDeleteRequestDTO.getSDVRuleId()))
                    .toList();

            fieldRelationsToDelete =
                    filterFieldRelations(fieldRelationsOfRule, fieldRelationDeleteRequestDTO.getFieldRelationIds());
        }

        fieldRelationRepository.deleteAll(fieldRelationsToDelete);
    }

    private List<FieldRelation> filterFieldRelations(List<FieldRelation> fieldRelations, List<Long> ids) {
        return fieldRelations
                .stream()
                .filter(f -> !ids.contains(f.getId()))
                .toList();
    }

    private void checkForSameIds(Long varId, Long srcId) {
        if (varId.equals(srcId)) {
            throw new IdenticalValueException("ids of variable and source must be different");
        }
    }

    public FieldRelation updateIfExists(Long modelId, Long varSummaryFieldId) {
        FieldRelation fieldRelation = getByModelIdAndVarSummaryFieldId(modelId, varSummaryFieldId);
        if (Objects.nonNull(fieldRelation)) {
            log.info(String.format("Field %s already exists in model, changing source...",
                    fieldRelation.getVarSummaryField().getName()));

            return fieldRelation;
        }
        return null;
    }

    private FieldRelation getByModelIdAndVarSummaryFieldId(Long modelId, Long varSummaryFieldId) {
        return fieldRelationRepository.findByModelStructIdAndVarSummaryFieldId(modelId, varSummaryFieldId);
    }

}
