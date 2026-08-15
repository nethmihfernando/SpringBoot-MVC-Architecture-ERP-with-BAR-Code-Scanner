package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name="purchase_order_items")
@Getter @Setter @NoArgsConstructor
public class PurchaseOrderItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="po_item_id")
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="po_id", nullable=false)
    @JsonIgnore
    private PurchaseOrder purchaseOrder;

    @NotNull @ManyToOne(optional=false)
    @JoinColumn(name="product_id", nullable=false)
    @JsonIgnoreProperties({"inventory"})
    private Product product;

    @NotNull @Positive @Column(nullable=false)
    private Integer quantity;

    @NotNull @Positive @Column(name="unit_price", nullable=false, precision=14, scale=2)
    private BigDecimal unitPrice;

    @Column(nullable=false, precision=14, scale=2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    public BigDecimal getSubtotal() {
        if (unitPrice == null || quantity == null) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @PrePersist @PreUpdate
    void calculate() { subtotal = getSubtotal(); }
}
