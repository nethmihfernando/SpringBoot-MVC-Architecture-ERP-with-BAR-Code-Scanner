package com.bci.productcrud.repository;

import com.bci.productcrud.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByPoNumber(String poNumber);

    boolean existsByPoNumber(String poNumber);
}
