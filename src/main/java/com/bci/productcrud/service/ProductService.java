package com.bci.productcrud.service;

import com.bci.productcrud.model.Product;

import java.util.List;

public interface ProductService {

    Product create(Product product);

    List<Product> findAll();

    Product findById(Long id);

    Product findByBarcode(String barcode);

    Product update(Long id, Product product);

    void delete(Long id);
}
