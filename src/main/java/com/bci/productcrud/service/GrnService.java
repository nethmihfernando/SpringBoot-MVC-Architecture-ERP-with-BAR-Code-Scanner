package com.bci.productcrud.service;

import com.bci.productcrud.model.GoodsReceivedNote;

import java.util.List;

public interface GrnService {

    GoodsReceivedNote create(GoodsReceivedNote grn);

    List<GoodsReceivedNote> findAll();

    GoodsReceivedNote findById(Long id);

    GoodsReceivedNote findByGrnNumber(String grnNumber);

    List<GoodsReceivedNote> findByPurchaseOrder(Long purchaseOrderId);
}
