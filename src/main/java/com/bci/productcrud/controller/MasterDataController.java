package com.bci.productcrud.controller;
import com.bci.productcrud.model.*;
import com.bci.productcrud.service.MasterDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class MasterDataController {
 private final MasterDataService s;
 @PostMapping("/roles") public ResponseEntity<Role> role(@Valid @RequestBody Role x){return ResponseEntity.status(HttpStatus.CREATED).body(s.saveRole(x));}
 @GetMapping("/roles") public List<Role> roles(){return s.roles();}
 @GetMapping("/roles/{id}") public Role role(@PathVariable Long id){return s.role(id);}
 @PostMapping("/users") public ResponseEntity<User> user(@Valid @RequestBody User x){return ResponseEntity.status(HttpStatus.CREATED).body(s.saveUser(x));}
 @GetMapping("/users") public List<User> users(){return s.users();}
 @GetMapping("/users/{id}") public User user(@PathVariable Long id){return s.user(id);}
 @PostMapping("/suppliers") public ResponseEntity<Supplier> supplier(@Valid @RequestBody Supplier x){return ResponseEntity.status(HttpStatus.CREATED).body(s.saveSupplier(x));}
 @GetMapping("/suppliers") public List<Supplier> suppliers(){return s.suppliers();}
 @GetMapping("/suppliers/{id}") public Supplier supplier(@PathVariable Long id){return s.supplier(id);}
 @PostMapping("/locations") public ResponseEntity<Location> location(@Valid @RequestBody Location x){return ResponseEntity.status(HttpStatus.CREATED).body(s.saveLocation(x));}
 @GetMapping("/locations") public List<Location> locations(){return s.locations();}
 @GetMapping("/locations/{id}") public Location location(@PathVariable Long id){return s.location(id);}
}
