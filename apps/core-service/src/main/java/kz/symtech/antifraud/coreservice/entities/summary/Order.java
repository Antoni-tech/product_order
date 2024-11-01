package kz.symtech.antifraud.coreservice.entities.summary;
import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.models.ModelStructComponents;
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
@Table(name = "orders")
public class Order extends BaseEntity {
//    private Long id;

    private Long total;

    private Long tax;

    @OneToOne
    @JoinColumn(name = "productOrderId", referencedColumnName = "id")
    private ProductOrder productOrder;
}
