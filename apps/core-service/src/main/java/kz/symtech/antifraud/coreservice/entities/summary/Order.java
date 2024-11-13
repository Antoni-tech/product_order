package kz.symtech.antifraud.coreservice.entities.summary;
import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.models.ModelStructComponents;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order extends BaseEntity {
    private Long total;
    private Long tax;

    // Связь "один ко многим" с ProductOrder
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOrder> productOrders = new ArrayList<>();



    // Геттеры и сеттеры
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public List<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(List<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }

    // Метод для добавления ProductOrder в заказ
    public void addProductOrder(ProductOrder productOrder) {
        if (productOrders == null) {
            productOrders = new ArrayList<>();
        }
        productOrders.add(productOrder);
        productOrder.setOrder(this);  // Устанавливаем обратную связь с заказом
    }

    // Метод для удаления ProductOrder из заказа
    public void removeProductOrder(ProductOrder productOrder) {
        productOrders.remove(productOrder);
        productOrder.setOrder(null); // Удаляем обратную связь
    }
}
