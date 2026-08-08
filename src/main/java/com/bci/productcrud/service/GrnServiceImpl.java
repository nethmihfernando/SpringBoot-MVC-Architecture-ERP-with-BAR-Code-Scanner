package com.bci.productcrud.service;

import com.bci.productcrud.exception.DuplicateGrnNumberException;
import com.bci.productcrud.exception.GrnNotFoundException;
import com.bci.productcrud.exception.InvalidGrnException;
import com.bci.productcrud.exception.PurchaseOrderNotFoundException;
import com.bci.productcrud.model.GoodsReceivedNote;
import com.bci.productcrud.model.GrnItem;
import com.bci.productcrud.model.GrnStatus;
import com.bci.productcrud.model.Product;
import com.bci.productcrud.model.PurchaseOrder;
import com.bci.productcrud.model.PurchaseOrderItem;
import com.bci.productcrud.model.PurchaseOrderStatus;
import com.bci.productcrud.repository.GrnRepository;
import com.bci.productcrud.repository.ProductRepository;
import com.bci.productcrud.repository.PurchaseOrderItemRepository;
import com.bci.productcrud.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GrnServiceImpl implements GrnService {

    private final GrnRepository grnRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final ProductRepository productRepository;

    @Override
    public GoodsReceivedNote create(GoodsReceivedNote request) {
        if (grnRepository.existsByGrnNumber(request.getGrnNumber())) {
            throw new DuplicateGrnNumberException("A GRN with number " + request.getGrnNumber() + " already exists");
        }
        if (request.getPurchaseOrder() == null || request.getPurchaseOrder().getId() == null) {
            throw new InvalidGrnException("A GRN must reference a valid purchase order id");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new InvalidGrnException("A GRN must contain at least one item");
        }

        PurchaseOrder po = purchaseOrderRepository.findById(request.getPurchaseOrder().getId())
                .orElseThrow(() -> new PurchaseOrderNotFoundException(
                        "Purchase order not found with id " + request.getPurchaseOrder().getId()));

        if (po.getStatus() != PurchaseOrderStatus.APPROVED && po.getStatus() != PurchaseOrderStatus.PARTIALLY_RECEIVED) {
            throw new InvalidGrnException(
                    "Goods can only be received against an APPROVED or PARTIALLY_RECEIVED purchase order; "
                            + po.getPoNumber() + " is " + po.getStatus());
        }

        GoodsReceivedNote grn = new GoodsReceivedNote();
        grn.setGrnNumber(request.getGrnNumber());
        grn.setPurchaseOrder(po);
        grn.setReceivedBy(request.getReceivedBy());
        grn.setReceivedDate(request.getReceivedDate() != null ? request.getReceivedDate() : Instant.now());
        grn.setNotes(request.getNotes());

        for (GrnItem itemRequest : request.getItems()) {
            if (itemRequest.getPurchaseOrderItem() == null || itemRequest.getPurchaseOrderItem().getId() == null) {
                throw new InvalidGrnException("Each GRN item must reference a valid purchase order item id");
            }

            PurchaseOrderItem poItem = purchaseOrderItemRepository.findById(itemRequest.getPurchaseOrderItem().getId())
                    .orElseThrow(() -> new InvalidGrnException(
                            "Purchase order item not found with id " + itemRequest.getPurchaseOrderItem().getId()));

            if (!poItem.getPurchaseOrder().getId().equals(po.getId())) {
                throw new InvalidGrnException(
                        "Purchase order item " + poItem.getId() + " does not belong to purchase order " + po.getPoNumber());
            }

            if (itemRequest.getQuantityReceived() == null || itemRequest.getQuantityReceived() <= 0) {
                throw new InvalidGrnException("Quantity received must be greater than zero");
            }

            int remaining = poItem.getQuantityRemaining();
            if (itemRequest.getQuantityReceived() > remaining) {
                throw new InvalidGrnException(
                        "Cannot receive " + itemRequest.getQuantityReceived() + " units for product "
                                + poItem.getProduct().getName() + "; only " + remaining + " units remain on the purchase order");
            }

            poItem.setQuantityReceived(poItem.getQuantityReceived() + itemRequest.getQuantityReceived());

            Product product = poItem.getProduct();
            product.setQuantity(product.getQuantity() + itemRequest.getQuantityReceived());
            productRepository.save(product);

            GrnItem item = new GrnItem();
            item.setPurchaseOrderItem(poItem);
            item.setQuantityReceived(itemRequest.getQuantityReceived());
            grn.addItem(item);
        }

        boolean fullyReceived = po.getItems().stream()
                .allMatch(item -> item.getQuantityReceived() >= item.getQuantityOrdered());
        po.setStatus(fullyReceived ? PurchaseOrderStatus.RECEIVED : PurchaseOrderStatus.PARTIALLY_RECEIVED);
        purchaseOrderRepository.save(po);

        grn.setStatus(fullyReceived ? GrnStatus.COMPLETED : GrnStatus.PARTIAL);

        return grnRepository.save(grn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoodsReceivedNote> findAll() {
        return grnRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsReceivedNote findById(Long id) {
        return grnRepository.findById(id)
                .orElseThrow(() -> new GrnNotFoundException("GRN not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsReceivedNote findByGrnNumber(String grnNumber) {
        return grnRepository.findByGrnNumber(grnNumber)
                .orElseThrow(() -> new GrnNotFoundException("GRN not found with number " + grnNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoodsReceivedNote> findByPurchaseOrder(Long purchaseOrderId) {
        return grnRepository.findByPurchaseOrderId(purchaseOrderId);
    }
}
