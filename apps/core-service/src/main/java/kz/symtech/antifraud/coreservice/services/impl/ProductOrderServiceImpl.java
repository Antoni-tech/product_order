package kz.symtech.antifraud.coreservice.services.impl;

import kz.symtech.antifraud.coreservice.entities.summary.ProductOrder;
import kz.symtech.antifraud.coreservice.repository.ProductRepository;
import kz.symtech.antifraud.coreservice.services.ProductOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductOrderServiceImpl implements ProductOrderService {

    private final ProductRepository productRepository;

    @Override
    public ProductOrder findById(Long id) {
        return null;
    }

    @Override
    public List<ProductOrder> findAll() {
        return null;
    }

    @Override
    public void save(ProductOrder productOrder) {

    }

    @Override
    public void update(ProductOrder productOrder) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
