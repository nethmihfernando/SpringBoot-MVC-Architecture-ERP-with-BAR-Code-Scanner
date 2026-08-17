package com.bci.productcrud.service;
import com.bci.productcrud.exception.ResourceNotFoundException;
import com.bci.productcrud.model.Inventory;
import com.bci.productcrud.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class InventoryService {
 private final InventoryRepository repo; private final MasterDataService master;
 public Inventory save(Inventory x){
   if(x.getProduct()==null||x.getProduct().getId()==null||x.getLocation()==null||x.getLocation().getId()==null) throw new IllegalArgumentException("product.id and location.id are required");
   x.setProduct(master.product(x.getProduct().getId())); x.setLocation(master.location(x.getLocation().getId()));
   return repo.save(x);
 }
 @Transactional(readOnly=true) public List<Inventory> all(){return repo.findAll();}
 @Transactional(readOnly=true) public Inventory get(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Inventory not found: "+id));}
 @Transactional(readOnly=true) public List<Inventory> byProduct(Long id){return repo.findByProductId(id);}
 @Transactional(readOnly=true) public List<Inventory> byLocation(Long id){return repo.findByLocationId(id);}
 public Inventory adjust(Long id,int delta){Inventory x=get(id); if(x.getQuantityOnHand()+delta<0) throw new IllegalArgumentException("Stock cannot become negative"); x.setQuantityOnHand(x.getQuantityOnHand()+delta); return repo.save(x);}
}
