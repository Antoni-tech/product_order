package kz.symtech.antifraud.coreservice.repository;

import kz.symtech.antifraud.coreservice.entities.summary.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
