package com.bci.productcrud.controller;
import com.bci.productcrud.model.Product;
import com.bci.productcrud.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/products") @RequiredArgsConstructor
public class ProductController {
 private final ProductService service;
 @PostMapping public ResponseEntity<Product> create(@Valid @RequestBody Product x){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));}
 @GetMapping public List<Product> all(){return service.findAll();}
 @GetMapping("/{id}") public Product get(@PathVariable Long id){return service.findById(id);}
 @GetMapping("/barcode/{barcode}") public Product barcode(@PathVariable String barcode){return service.findByBarcode(barcode);}
 @PutMapping("/{id}") public Product update(@PathVariable Long id,@Valid @RequestBody Product x){return service.update(id,x);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
}
