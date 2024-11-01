package kz.symtech.antifraud.coreservice.entities.summary;

import jakarta.persistence.*;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class ProductOrder extends BaseEntity {
//    @OneToOne(mappedBy = "product")
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long quantity;
}
