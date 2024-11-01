package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.entities.models.ModelStructComponents;
import kz.symtech.antifraud.coreservice.services.ConnectorUtilService;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.SummaryFieldCacheDTO;
import kz.symtech.antifraud.models.enums.ModelComponentEnumField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ModelComponentCacheMapper implements Function<ModelStructComponents, ComponentModel> {

    private final SummaryFieldMapper summaryFieldMapper;
    private final ConnectorUtilService connectorUtilService;
    private final ComponentElementMapper componentElementMapper;
    @Override
    public ComponentModel apply(ModelStructComponents component) {
        List<SummaryFieldCacheDTO> summaryFieldCacheDTOS = component.getSummaryDataVersion().getSummaryFields()
                .stream().map(summaryFieldMapper).toList();

        Integer queueNumber = connectorUtilService.getComponentElement(component, ModelComponentEnumField.QUEUE_NUMBER);
        Boolean launchSecondStage = connectorUtilService.getComponentElement(component, ModelComponentEnumField.LAUNCH_SECOND_STAGE);
        Boolean test = connectorUtilService.getComponentElement(component, ModelComponentEnumField.TEST);

        return ComponentModel
                .builder()
                .summaryDataVersionId(component.getSummaryDataVersion().getId())
                .summaryDataId(component.getSummaryDataVersion().getSummaryData().getId())
                .queueNumber(queueNumber == null ? -1 : queueNumber)
                .launchSecondStage(launchSecondStage != null && launchSecondStage)
                .daysRemaining(component.getDaysRemaining())
                .saveResult(component.getDaysRemaining() > 0)
                .resultIncremental(component.getResultIncremental())
                .test(test)
                .isActive(component.getSummaryDataVersion().getIsActive())
                .validateFields(component.getSummaryDataVersion().getValidateFields())
                .summaryFields(summaryFieldCacheDTOS)
                .elements(component.getModelComponentElements().stream().map(componentElementMapper).toList())
                .build();
    }
}
