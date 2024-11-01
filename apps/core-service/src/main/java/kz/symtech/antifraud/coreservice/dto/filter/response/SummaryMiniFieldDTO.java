package kz.symtech.antifraud.coreservice.dto.filter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class SummaryMiniFieldDTO extends SummaryFieldDTO {
    private List<SummaryMiniFieldDTO> children;
}
