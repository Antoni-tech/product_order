package kz.symtech.antifraud.coreservice.dto;

import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryDataResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SummaryDataListResponseDTO {
    private Long totalCount;
    List<SummaryDataResponseDTO> list;
}
