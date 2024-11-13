package kz.symtech.antifraud.coreservice.services.impl;

import jakarta.transaction.Transactional;
import kz.symtech.antifraud.coreservice.dto.ProductOrderRequest;
import kz.symtech.antifraud.coreservice.dto.ProductOrderRequestDTO;
import kz.symtech.antifraud.coreservice.entities.summary.Order;
import kz.symtech.antifraud.coreservice.entities.summary.Product;
import kz.symtech.antifraud.coreservice.entities.summary.ProductOrder;
import kz.symtech.antifraud.coreservice.repository.OrderRepository;
import kz.symtech.antifraud.coreservice.repository.ProductOrderRepository;
import kz.symtech.antifraud.coreservice.repository.ProductRepository;
import kz.symtech.antifraud.coreservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor //
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;

    @Transactional
    public Long saveOrder(ProductOrderRequestDTO requestDTO) {
        Order order = new Order();
        order.setTotal(0L);  // Устанавливаем начальное значение для total
        order.setTax(0L);

        for (ProductOrderRequest productOrderRequest : requestDTO.getProductOrders()) {
            Optional<Product> productOpt = productRepository.findById(productOrderRequest.getProductId());

            if (productOpt.isPresent()) {
                Product product = productOpt.get();

                ProductOrder productOrder = new ProductOrder();
                productOrder.setProduct(product);
                productOrder.setQuantity(productOrderRequest.getQuantity());

                // Расчёт налога и суммы
                Long totalTax = taxCount(productOrder.getQuantity(), product.getPrice());
                Long total = totalCount(totalTax, productOrder.getQuantity(), product.getPrice());

                order.setTax(order.getTax() + totalTax);  // Добавляем налог к общему заказу
                order.setTotal(order.getTotal() + total);  // Добавляем сумму к общему заказу

                // Добавляем ProductOrder в заказ
                order.addProductOrder(productOrder);
            } else {
                throw new RuntimeException("Product not found with id: " + productOrderRequest.getProductId());
            }
        }

        order = orderRepository.save(order);  // Сохраняем заказ вместе с ProductOrder

        return order.getId();  // Возвращаем ID созданного заказа
    }


    // Метод для расчёта налога
    private Long taxCount(Long quantity, Long price) {
        double taxRate = 0.1;  // 10% налог
        double totalTax = quantity * price * taxRate;
        return (long) totalTax;
    }

    // Метод для расчёта итоговой суммы
    private Long totalCount(Long totalTax, Long quantity, Long price) {
        double totalWithoutTax = quantity * price;
        double total = totalWithoutTax + totalTax;
        return (long) total;
    }

    @Override
    public Long save(ProductOrderRequestDTO order) {
        return saveOrder(order);
    }

    @Override
    public Long save(ProductOrderRequestDTO requestDTO, Long orderId) {
        return 0L;
    }

    @Override
    public void deleteOrderById(Long id) {

    }

    @Override
    public void deleteOrdersByIds(List<Long> orderIds) {

    }

    @Override
    public List<Order> getAllOrders() {
        return List.of();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return Optional.empty();
    }
}


