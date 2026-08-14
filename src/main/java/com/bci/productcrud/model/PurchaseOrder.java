package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="purchase_orders")
@Getter @Setter @NoArgsConstructor
public class PurchaseOrder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="po_id")
    private Long id;

    @NotNull @ManyToOne(optional=false)
    @JoinColumn(name="supplier_id", nullable=false)
    @JsonIgnoreProperties({"purchaseOrders"})
    private Supplier supplier;

    @NotNull @ManyToOne(optional=false)
    @JoinColumn(name="created_by", nullable=false)
    @JsonIgnoreProperties({"role"})
    private User createdBy;

    @ManyToOne
    @JoinColumn(name="approved_by")
    @JsonIgnoreProperties({"role"})
    private User approvedBy;

    @Column(name="po_date", nullable=false)
    private LocalDate poDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private PurchaseOrderStatus status = PurchaseOrderStatus.PENDING;

    @OneToMany(mappedBy="purchaseOrder", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<PurchaseOrderItem> items = new ArrayList<>();

    @Column(name="total_amount", nullable=false, precision=14, scale=2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public void addItem(PurchaseOrderItem item) {
        items.add(item);
        item.setPurchaseOrder(this);
    }

    public void recalculateTotal() {
        totalAmount = items.stream()
                .map(PurchaseOrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PrePersist @PreUpdate
    void beforeSave() {
        if (poDate == null) poDate = LocalDate.now();
        recalculateTotal();
    }
}
