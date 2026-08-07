package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "purchase_order_items")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    @JsonIgnore
    private PurchaseOrder purchaseOrder;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer quantityOrdered;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private Integer quantityReceived = 0;

    public Double getLineTotal() {
        return unitPrice * quantityOrdered;
    }

    public Integer getQuantityRemaining() {
        return quantityOrdered - quantityReceived;
    }
}
