package com.bci.productcrud.repository;
import com.bci.productcrud.model.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long> {
    List<GoodsReceipt> findByPurchaseOrderId(Long purchaseOrderId);
}
