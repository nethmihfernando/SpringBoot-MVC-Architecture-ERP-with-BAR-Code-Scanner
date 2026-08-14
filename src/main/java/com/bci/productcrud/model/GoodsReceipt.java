package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="goods_receipts")
@Getter @Setter @NoArgsConstructor
public class GoodsReceipt {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="grn_id")
    private Long id;

    @NotNull @ManyToOne(optional=false)
    @JoinColumn(name="po_id", nullable=false)
    @JsonIgnoreProperties({"items"})
    private PurchaseOrder purchaseOrder;

    @NotNull @ManyToOne(optional=false)
    @JoinColumn(name="received_by", nullable=false)
    @JsonIgnoreProperties({"role"})
    private User receivedBy;

    @Column(name="received_date", nullable=false)
    private LocalDate receivedDate;

    private String status = "COMPLETED";

    @OneToMany(mappedBy="goodsReceipt", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<GoodsReceiptItem> items = new ArrayList<>();

    public void addItem(GoodsReceiptItem item) {
        items.add(item);
        item.setGoodsReceipt(this);
    }

    @PrePersist void defaults() { if (receivedDate == null) receivedDate = LocalDate.now(); }
}
