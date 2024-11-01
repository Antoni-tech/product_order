package kz.symtech.antifraud.coreservice.services;

import jakarta.transaction.Transactional;
import kz.symtech.antifraud.coreservice.dto.ProductOrderRequestDTO;
import kz.symtech.antifraud.coreservice.entities.summary.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Long save(ProductOrderRequestDTO order);


    // Метод для сохранения ProductOrder с привязкой к уже существующему заказу
    @Transactional
    Long save(ProductOrderRequestDTO requestDTO, Long orderId);

    void deleteOrderById(Long id);

    void deleteOrdersByIds(List<Long> orderIds);

    List<Order> getAllOrders();

    Optional<Order> getOrderById(Long id);
}
