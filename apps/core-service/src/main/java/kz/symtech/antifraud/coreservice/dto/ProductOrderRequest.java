package kz.symtech.antifraud.coreservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductOrderRequest {
    private Long productId;
    private Long quantity;
}


