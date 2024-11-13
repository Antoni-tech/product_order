package kz.symtech.antifraud.coreservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;



@Getter
@NoArgsConstructor
@Data
public class ProductOrderRequestDTO {
    private List<ProductOrderRequest> productOrders = new ArrayList<>(); // Инициализация пустым списком
}
