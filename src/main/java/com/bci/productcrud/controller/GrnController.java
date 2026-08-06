package com.bci.productcrud.controller;

import com.bci.productcrud.model.GoodsReceivedNote;
import com.bci.productcrud.service.GrnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grn")
@RequiredArgsConstructor
public class GrnController {

    private final GrnService grnService;

    @PostMapping
    public ResponseEntity<GoodsReceivedNote> create(@Valid @RequestBody GoodsReceivedNote grn) {
        GoodsReceivedNote created = grnService.create(grn);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<GoodsReceivedNote> findAll() {
        return grnService.findAll();
    }

    @GetMapping("/{id}")
    public GoodsReceivedNote findById(@PathVariable Long id) {
        return grnService.findById(id);
    }

    @GetMapping("/number/{grnNumber}")
    public GoodsReceivedNote findByGrnNumber(@PathVariable String grnNumber) {
        return grnService.findByGrnNumber(grnNumber);
    }

    @GetMapping("/purchase-order/{purchaseOrderId}")
    public List<GoodsReceivedNote> findByPurchaseOrder(@PathVariable Long purchaseOrderId) {
        return grnService.findByPurchaseOrder(purchaseOrderId);
    }
}
