package com.bci.productcrud.controller;

import com.bci.productcrud.model.PurchaseOrder;
import com.bci.productcrud.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    public ResponseEntity<PurchaseOrder> create(@Valid @RequestBody PurchaseOrder purchaseOrder) {
        PurchaseOrder created = purchaseOrderService.create(purchaseOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<PurchaseOrder> findAll() {
        return purchaseOrderService.findAll();
    }

    @GetMapping("/{id}")
    public PurchaseOrder findById(@PathVariable Long id) {
        return purchaseOrderService.findById(id);
    }

    @GetMapping("/number/{poNumber}")
    public PurchaseOrder findByPoNumber(@PathVariable String poNumber) {
        return purchaseOrderService.findByPoNumber(poNumber);
    }

    @PutMapping("/{id}")
    public PurchaseOrder update(@PathVariable Long id, @Valid @RequestBody PurchaseOrder purchaseOrder) {
        return purchaseOrderService.update(id, purchaseOrder);
    }

    @PatchMapping("/{id}/approve")
    public PurchaseOrder approve(@PathVariable Long id) {
        return purchaseOrderService.approve(id);
    }

    @PatchMapping("/{id}/cancel")
    public PurchaseOrder cancel(@PathVariable Long id) {
        return purchaseOrderService.cancel(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        purchaseOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
