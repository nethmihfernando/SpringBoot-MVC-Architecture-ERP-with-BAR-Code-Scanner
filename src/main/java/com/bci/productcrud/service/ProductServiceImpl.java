package com.bci.productcrud.service;

import com.bci.productcrud.exception.DuplicateBarcodeException;
import com.bci.productcrud.exception.ProductNotFoundException;
import com.bci.productcrud.model.Product;
import com.bci.productcrud.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product create(Product product) {
        if (productRepository.existsByBarcode(product.getBarcode())) {
            throw new DuplicateBarcodeException("A product with barcode " + product.getBarcode() + " already exists");
        }
        product.setId(null);
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Product findByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with barcode " + barcode));
    }

    @Override
    public Product update(Long id, Product request) {
        Product product = findById(id);

        productRepository.findByBarcode(request.getBarcode())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateBarcodeException("A product with barcode " + request.getBarcode() + " already exists");
                });

        product.setBarcode(request.getBarcode());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
    }
}
