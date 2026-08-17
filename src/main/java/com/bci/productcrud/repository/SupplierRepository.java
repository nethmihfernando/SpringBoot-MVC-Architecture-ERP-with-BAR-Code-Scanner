package com.bci.productcrud.repository;
import com.bci.productcrud.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SupplierRepository extends JpaRepository<Supplier, Long> {}
