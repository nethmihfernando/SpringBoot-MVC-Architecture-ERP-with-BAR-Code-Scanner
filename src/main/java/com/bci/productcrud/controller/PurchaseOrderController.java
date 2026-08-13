package com.bci.productcrud.controller;
import com.bci.productcrud.model.PurchaseOrder;
import com.bci.productcrud.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/purchase-orders") @RequiredArgsConstructor
public class PurchaseOrderController {
 private final PurchaseOrderService s;
 @PostMapping public ResponseEntity<PurchaseOrder> create(@Valid @RequestBody PurchaseOrder x){return ResponseEntity.status(HttpStatus.CREATED).body(s.create(x));}
 @GetMapping public List<PurchaseOrder> all(){return s.all();}
 @GetMapping("/{id}") public PurchaseOrder get(@PathVariable Long id){return s.get(id);}
 @GetMapping("/supplier/{supplierId}") public List<PurchaseOrder> supplier(@PathVariable Long supplierId){return s.bySupplier(supplierId);}
 @PatchMapping("/{id}/approve") public PurchaseOrder approve(@PathVariable Long id,@RequestParam Long approvedBy){return s.approve(id,approvedBy);}
 @PatchMapping("/{id}/cancel") public PurchaseOrder cancel(@PathVariable Long id){return s.cancel(id);}
}
