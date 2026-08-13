package com.bci.productcrud.controller;
import com.bci.productcrud.model.Payment;
import com.bci.productcrud.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/payments") @RequiredArgsConstructor
public class PaymentController {
 private final PaymentService s;
 @PostMapping public ResponseEntity<Payment> create(@Valid @RequestBody Payment x){return ResponseEntity.status(HttpStatus.CREATED).body(s.create(x));}
 @GetMapping public List<Payment> all(){return s.all();}
 @GetMapping("/{id}") public Payment get(@PathVariable Long id){return s.get(id);}
 @GetMapping("/invoice/{invoiceId}") public List<Payment> invoice(@PathVariable Long invoiceId){return s.byInvoice(invoiceId);}
}
