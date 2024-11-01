package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.dto.filter.response.ModelStructComponentsFilterResponseDTO;
import kz.symtech.antifraud.coreservice.entities.models.ModelStructComponents;
import kz.symtech.antifraud.coreservice.services.ConnectorUtilService;
import kz.symtech.antifraud.models.enums.ModelComponentEnumField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ModelStructComponentsFilterResponseDTOMapper  implements Function<ModelStructComponents, ModelStructComponentsFilterResponseDTO> {
    private final ConnectorUtilService connectorUtilService;
    @Override
    public ModelStructComponentsFilterResponseDTO apply(ModelStructComponents modelStructComponents) {
        Integer queueNumber = connectorUtilService.getComponentElement(modelStructComponents, ModelComponentEnumField.QUEUE_NUMBER);
        Boolean launchSecondStage = connectorUtilService.getComponentElement(modelStructComponents, ModelComponentEnumField.LAUNCH_SECOND_STAGE);
        
        return ModelStructComponentsFilterResponseDTO.builder()
                .modelId(modelStructComponents.getModelStruct().getId())
                .daysRemaining(modelStructComponents.getDaysRemaining())
                .launchSecondStage(launchSecondStage)
                .queueNumber(queueNumber == null ? 0 : queueNumber)
                .resultIncremental(modelStructComponents.getResultIncremental())
                .modelName(modelStructComponents.getModelStruct().getSummaryDataVersion().getName())
                .build();
    }
}
