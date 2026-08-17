package com.bci.productcrud.service;
import com.bci.productcrud.exception.ResourceNotFoundException;
import com.bci.productcrud.model.*;
import com.bci.productcrud.repository.GoodsReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class GoodsReceiptService {
 private final GoodsReceiptRepository repo; private final PurchaseOrderService poService; private final MasterDataService master; private final InventoryService inventory;
 public GoodsReceipt create(GoodsReceipt x){
   if(x.getPurchaseOrder()==null||x.getPurchaseOrder().getId()==null||x.getReceivedBy()==null||x.getReceivedBy().getId()==null) throw new IllegalArgumentException("purchaseOrder.id and receivedBy.id are required");
   PurchaseOrder po=poService.get(x.getPurchaseOrder().getId());
   if(po.getStatus()!=PurchaseOrderStatus.APPROVED && po.getStatus()!=PurchaseOrderStatus.PARTIALLY_RECEIVED) throw new IllegalArgumentException("Goods can only be received for APPROVED or PARTIALLY_RECEIVED orders");
   x.setPurchaseOrder(po); x.setReceivedBy(master.user(x.getReceivedBy().getId()));
   if(x.getItems()==null||x.getItems().isEmpty()) throw new IllegalArgumentException("Goods receipt requires at least one item");
   for(GoodsReceiptItem i:x.getItems()){
     if(i.getProduct()==null||i.getProduct().getId()==null) throw new IllegalArgumentException("Each receipt item requires product.id");
     i.setProduct(master.product(i.getProduct().getId())); i.setGoodsReceipt(x);
     if(i.getBarcode()==null) i.setBarcode(i.getProduct().getBarcode());
     if(i.getReceivedDate()==null) i.setReceivedDate(x.getReceivedDate());
     // Inventory is location-aware. The receipt model has no location FK in the supplied ERD,
     // so stock is not silently assigned to a location here; use /api/inventory to maintain branch stock.
   }
   return repo.save(x);
 }
 @Transactional(readOnly=true) public List<GoodsReceipt> all(){return repo.findAll();}
 @Transactional(readOnly=true) public GoodsReceipt get(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Goods receipt not found: "+id));}
 @Transactional(readOnly=true) public List<GoodsReceipt> byPurchaseOrder(Long id){return repo.findByPurchaseOrderId(id);}
}
