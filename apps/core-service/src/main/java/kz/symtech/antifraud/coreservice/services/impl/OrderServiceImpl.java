package kz.symtech.antifraud.coreservice.services.impl;

import jakarta.transaction.Transactional;
import kz.symtech.antifraud.coreservice.dto.ProductOrderRequestDTO;
import kz.symtech.antifraud.coreservice.entities.summary.Order;
import kz.symtech.antifraud.coreservice.entities.summary.Product;
import kz.symtech.antifraud.coreservice.entities.summary.ProductOrder;
import kz.symtech.antifraud.coreservice.repository.OrderRepository;
import kz.symtech.antifraud.coreservice.repository.ProductOrderRepository;
import kz.symtech.antifraud.coreservice.repository.ProductRepository;
import kz.symtech.antifraud.coreservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;

    // Метод для сохранения заказа (Order) с расчётом налога и итоговой суммы
    public Long saveOrder(ProductOrderRequestDTO requestDTO) {
        Optional<Product> product = productRepository.findById(requestDTO.getProductId());

        if (product.isPresent()) {
            Product product1 = product.get();

            // Создаем новый заказ
            Order order1 = new Order();

            // Расчет налога и тотала
            Long totalTax = taxCount(requestDTO.getQuantity(), product1.getPrice());
            Long total = totalCount(totalTax, requestDTO.getQuantity(), product1.getPrice());

            // Устанавливаем рассчитанные значения в заказ
            order1.setTax(totalTax);
            order1.setTotal(total);

            // Сохраняем заказ (Order)
            order1 = orderRepository.save(order1);  // После этого order1 будет иметь сгенерированный ID

            ProductOrder productOrder = new ProductOrder();
            productOrder.setQuantity(requestDTO.getQuantity());
            productOrder.setOrder(order1);  // Устанавливаем связь
            productOrder.setProduct(product1);

            // Сохраняем объект ProductOrder
            productOrder = productOrderRepository.save(productOrder);

            // Связываем Order с ProductOrder
            order1.setProductOrder(productOrder);
            orderRepository.save(order1);

            return order1.getId();  // Возвращаем ID созданного заказа
        } else {
            throw new RuntimeException("Product not found with id: " + requestDTO.getProductId());
        }
    }

    // Метод для сохранения ProductOrder с привязкой к уже существующему заказу
    @Transactional
    @Override
    public Long save(ProductOrderRequestDTO requestDTO, Long orderId) {
        Optional<Product> product = productRepository.findById(requestDTO.getProductId());
        Optional<Order> order = orderRepository.findById(orderId);  // Находим заказ по его ID

        if (product.isPresent() && order.isPresent()) {
            Product product1 = product.get();
            Order order1 = order.get();  // Используем найденный заказ

            // Создаем объект ProductOrder и связываем его с найденным заказом и продуктом
            ProductOrder productOrder = new ProductOrder();
            productOrder.setQuantity(requestDTO.getQuantity());
            productOrder.setOrder(order1);  // Устанавливаем заказ
            productOrder.setProduct(product1);  // Устанавливаем продукт

            // Сохраняем объект ProductOrder


            return productOrder.getId();  // Возвращаем ID созданного ProductOrder
        } else {
            throw new RuntimeException("Product or Order not found.");
        }
    }

    // Вспомогательные методы для расчета налога и итоговой суммы
    private Long taxCount(Long quantity, Long price) {
        double taxRate = 0.1;  // 10%
        double totalTax = quantity * price * taxRate;
        return (long) totalTax;
    }

    private Long totalCount(Long totalTax, Long quantity, Long price) {
        double totalWithoutTax = quantity * price;
        double total = totalWithoutTax + totalTax;
        return (long) total;
    }



    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
        // Логика удаления заказа
    }

    @Override
    public void deleteOrdersByIds(List<Long> orderIds) {
        orderRepository.deleteAllById(orderIds);
    }

    @Override
    public List<Order> getAllOrders() {
        return null; // Логика получения всех заказов
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return Optional.empty(); // Логика получения заказа по ID
    }

    @Override
    public Long save(ProductOrderRequestDTO requestDTO) {
        // 1. Сохраняем заказ (Order) и получаем его ID
        Long orderId = saveOrder(requestDTO);

        // 2. Сохраняем ProductOrder, привязывая его к сохранённому заказу
        save(requestDTO, orderId);

        return orderId;  // Возвращаем ID созданного заказа
    }
}

