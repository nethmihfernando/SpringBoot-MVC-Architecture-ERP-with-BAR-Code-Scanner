package com.bci.productcrud.repository;
import com.bci.productcrud.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByLocationId(Long locationId);
    List<Inventory> findByProductId(Long productId);
    Optional<Inventory> findByProductIdAndLocationId(Long productId, Long locationId);
}
