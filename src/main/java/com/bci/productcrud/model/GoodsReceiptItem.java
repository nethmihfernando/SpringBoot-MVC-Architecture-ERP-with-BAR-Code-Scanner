package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name="goods_receipt_items")
@Getter @Setter @NoArgsConstructor
public class GoodsReceiptItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="grn_item_id")
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="grn_id", nullable=false)
    @JsonIgnore
    private GoodsReceipt goodsReceipt;

    @NotNull @ManyToOne(optional=false)
    @JoinColumn(name="product_id", nullable=false)
    @JsonIgnoreProperties({"inventory"})
    private Product product;

    @NotNull @Positive @Column(nullable=false)
    private Integer quantity;

    @Column(name="received_date", nullable=false)
    private LocalDate receivedDate;

    private String barcode;

    @PrePersist void defaults() {
        if (receivedDate == null) receivedDate = goodsReceipt != null ? goodsReceipt.getReceivedDate() : LocalDate.now();
        if (barcode == null && product != null) barcode = product.getBarcode();
    }
}
