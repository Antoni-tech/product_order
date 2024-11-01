package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.dto.ModelConnectorOutputDTO;
import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelConnectorOutputDTOMapper {
    public ModelConnectorOutputDTO buildModelConnectorOutputDTO(ModelConnectorOutput modelConnectorOutput) {
        return ModelConnectorOutputDTO.builder()
                .id(modelConnectorOutput.getSummaryDataVersion().getId())
                .type(modelConnectorOutput.getSummaryDataVersion().getSummaryData().getType())
                .name(modelConnectorOutput.getSummaryDataVersion().getName())
                .createUserId(modelConnectorOutput.getSummaryDataVersion().getUserUpdateId())
                .checkMainAttributes(modelConnectorOutput.getCheckMainAttributes())
                .dataFormat(modelConnectorOutput.getDataFormat())
                .url(modelConnectorOutput.getUrl())
                .build();
    }
}
