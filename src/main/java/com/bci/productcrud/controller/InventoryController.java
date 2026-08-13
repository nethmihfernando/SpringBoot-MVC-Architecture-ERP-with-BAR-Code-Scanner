package com.bci.productcrud.controller;
import com.bci.productcrud.model.Inventory;
import com.bci.productcrud.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/inventory") @RequiredArgsConstructor
public class InventoryController {
 private final InventoryService s;
 @PostMapping public Inventory create(@Valid @RequestBody Inventory x){return s.save(x);}
 @GetMapping public List<Inventory> all(){return s.all();}
 @GetMapping("/{id}") public Inventory get(@PathVariable Long id){return s.get(id);}
 @GetMapping("/product/{productId}") public List<Inventory> product(@PathVariable Long productId){return s.byProduct(productId);}
 @GetMapping("/location/{locationId}") public List<Inventory> location(@PathVariable Long locationId){return s.byLocation(locationId);}
 @PatchMapping("/{id}/adjust") public Inventory adjust(@PathVariable Long id,@RequestParam int delta){return s.adjust(id,delta);}
}
