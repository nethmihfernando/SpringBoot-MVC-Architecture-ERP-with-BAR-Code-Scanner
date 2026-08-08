package com.bci.productcrud.repository;

import com.bci.productcrud.model.GoodsReceivedNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GrnRepository extends JpaRepository<GoodsReceivedNote, Long> {

    Optional<GoodsReceivedNote> findByGrnNumber(String grnNumber);

    boolean existsByGrnNumber(String grnNumber);

    List<GoodsReceivedNote> findByPurchaseOrderId(Long purchaseOrderId);
}
