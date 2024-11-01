package kz.symtech.antifraud.coreservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductOrderRequestDTO {
    private Long total;
    private Long tax;
    private Long quantity;
    private Long productId;
}
