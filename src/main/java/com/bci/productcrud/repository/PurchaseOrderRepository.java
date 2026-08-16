package com.bci.productcrud.repository;
import com.bci.productcrud.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findBySupplierId(Long supplierId);
}
