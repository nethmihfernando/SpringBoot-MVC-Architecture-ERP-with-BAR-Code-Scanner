package com.bci.productcrud.controller;
import com.bci.productcrud.model.SalesPaymentReceipt;
import com.bci.productcrud.service.SalesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/sales-receipts") @RequiredArgsConstructor
public class SalesController {
 private final SalesService s;
 @PostMapping public ResponseEntity<SalesPaymentReceipt> create(@Valid @RequestBody SalesPaymentReceipt x){return ResponseEntity.status(HttpStatus.CREATED).body(s.create(x));}
 @GetMapping public List<SalesPaymentReceipt> all(){return s.all();}
 @GetMapping("/{id}") public SalesPaymentReceipt get(@PathVariable Long id){return s.get(id);}
}
