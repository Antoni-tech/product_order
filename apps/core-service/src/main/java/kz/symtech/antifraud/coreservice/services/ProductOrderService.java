package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.entities.summary.ProductOrder;

import java.util.List;
import java.util.Map;

public interface ProductOrderService {

        ProductOrder findById(Long id);

        List<ProductOrder> findAll();

        void save(ProductOrder productOrder);

        void update(ProductOrder productOrder);

        void deleteById(Long id);
}
