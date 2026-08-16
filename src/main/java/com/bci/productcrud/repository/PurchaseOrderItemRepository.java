package com.bci.productcrud.repository;
import com.bci.productcrud.model.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {}
