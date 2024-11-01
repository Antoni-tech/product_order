package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.dto.DataStructureDTO;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataStructureDTOMapper {
    public DataStructureDTO buildDataStructureDTOMapperDTO(SummaryDataVersion summaryDataVersion) {
        return DataStructureDTO.builder()
                .id(summaryDataVersion.getId())
                .type(summaryDataVersion.getSummaryData().getType())
                .name(summaryDataVersion.getName())
                .description(summaryDataVersion.getDescription())
                .createUserId(summaryDataVersion.getUserUpdateId())
                .build();
    }
}
