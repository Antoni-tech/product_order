package kz.symtech.antifraud.coreservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class SummaryDataFilterRequestDTO {
    private Date startCreateDate;
    private Date endCreateDate;
    private Date startUpdateDate;
    private Date endUpdateDate;
    private String name;
    private String shortDescription;
    private Long modelId;
    private Boolean showFields;
}
