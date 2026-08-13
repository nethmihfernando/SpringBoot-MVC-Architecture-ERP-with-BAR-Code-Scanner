package com.bci.productcrud.controller;
import com.bci.productcrud.model.GoodsReceipt;
import com.bci.productcrud.service.GoodsReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/goods-receipts") @RequiredArgsConstructor
public class GoodsReceiptController {
 private final GoodsReceiptService s;
 @PostMapping public ResponseEntity<GoodsReceipt> create(@Valid @RequestBody GoodsReceipt x){return ResponseEntity.status(HttpStatus.CREATED).body(s.create(x));}
 @GetMapping public List<GoodsReceipt> all(){return s.all();}
 @GetMapping("/{id}") public GoodsReceipt get(@PathVariable Long id){return s.get(id);}
 @GetMapping("/purchase-order/{poId}") public List<GoodsReceipt> byPo(@PathVariable Long poId){return s.byPurchaseOrder(poId);}
}
