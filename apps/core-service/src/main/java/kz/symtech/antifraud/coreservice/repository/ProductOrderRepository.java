package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.summary.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
}
