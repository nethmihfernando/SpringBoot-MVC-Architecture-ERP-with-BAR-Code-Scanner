package com.bci.productcrud.service;

import com.bci.productcrud.exception.DuplicatePoNumberException;
import com.bci.productcrud.exception.InvalidPurchaseOrderStateException;
import com.bci.productcrud.exception.ProductNotFoundException;
import com.bci.productcrud.exception.PurchaseOrderNotFoundException;
import com.bci.productcrud.model.Product;
import com.bci.productcrud.model.PurchaseOrder;
import com.bci.productcrud.model.PurchaseOrderItem;
import com.bci.productcrud.model.PurchaseOrderStatus;
import com.bci.productcrud.repository.ProductRepository;
import com.bci.productcrud.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;

    @Override
    public PurchaseOrder create(PurchaseOrder request) {
        if (purchaseOrderRepository.existsByPoNumber(request.getPoNumber())) {
            throw new DuplicatePoNumberException(
                    "A purchase order with number " + request.getPoNumber() + " already exists");
        }

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber(request.getPoNumber());
        po.setSupplierName(request.getSupplierName());
        po.setOrderDate(request.getOrderDate() != null ? request.getOrderDate() : LocalDate.now());
        po.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        po.setStatus(PurchaseOrderStatus.PENDING);

        request.getItems().forEach(itemRequest -> po.addItem(buildItem(itemRequest)));

        return purchaseOrderRepository.save(po);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findAll() {
        return purchaseOrderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrder findById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Purchase order not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrder findByPoNumber(String poNumber) {
        return purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() -> new PurchaseOrderNotFoundException(
                        "Purchase order not found with number " + poNumber));
    }

    @Override
    public PurchaseOrder update(Long id, PurchaseOrder request) {
        PurchaseOrder po = findById(id);

        if (po.getStatus() != PurchaseOrderStatus.PENDING) {
            throw new InvalidPurchaseOrderStateException(
                    "Purchase order " + po.getPoNumber() + " cannot be edited once it is " + po.getStatus());
        }

        purchaseOrderRepository.findByPoNumber(request.getPoNumber())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicatePoNumberException(
                            "A purchase order with number " + request.getPoNumber() + " already exists");
                });

        po.setPoNumber(request.getPoNumber());
        po.setSupplierName(request.getSupplierName());
        po.setExpectedDeliveryDate(request.getExpectedDeliveryDate());

        po.getItems().clear();
        request.getItems().forEach(itemRequest -> po.addItem(buildItem(itemRequest)));

        return purchaseOrderRepository.save(po);
    }

    @Override
    public PurchaseOrder approve(Long id) {
        PurchaseOrder po = findById(id);
        if (po.getStatus() != PurchaseOrderStatus.PENDING) {
            throw new InvalidPurchaseOrderStateException(
                    "Only a PENDING purchase order can be approved; " + po.getPoNumber() + " is " + po.getStatus());
        }
        po.setStatus(PurchaseOrderStatus.APPROVED);
        return purchaseOrderRepository.save(po);
    }

    @Override
    public PurchaseOrder cancel(Long id) {
        PurchaseOrder po = findById(id);
        if (po.getStatus() == PurchaseOrderStatus.RECEIVED) {
            throw new InvalidPurchaseOrderStateException(
                    "Purchase order " + po.getPoNumber() + " has already been fully received and cannot be cancelled");
        }
        po.setStatus(PurchaseOrderStatus.CANCELLED);
        return purchaseOrderRepository.save(po);
    }

    @Override
    public void delete(Long id) {
        PurchaseOrder po = findById(id);
        if (po.getStatus() != PurchaseOrderStatus.PENDING) {
            throw new InvalidPurchaseOrderStateException(
                    "Only a PENDING purchase order can be deleted; " + po.getPoNumber() + " is " + po.getStatus());
        }
        purchaseOrderRepository.delete(po);
    }

    private PurchaseOrderItem buildItem(PurchaseOrderItem itemRequest) {
        if (itemRequest.getProduct() == null || itemRequest.getProduct().getId() == null) {
            throw new ProductNotFoundException("Each purchase order item must reference a valid product id");
        }
        Product product = productRepository.findById(itemRequest.getProduct().getId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with id " + itemRequest.getProduct().getId()));

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setProduct(product);
        item.setQuantityOrdered(itemRequest.getQuantityOrdered());
        item.setUnitPrice(itemRequest.getUnitPrice());
        return item;
    }
}
