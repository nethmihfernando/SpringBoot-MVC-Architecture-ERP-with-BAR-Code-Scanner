package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "grn_items")
@Getter
@Setter
@NoArgsConstructor
public class GrnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "grn_id", nullable = false)
    @JsonIgnore
    private GoodsReceivedNote grn;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "purchase_order_item_id", nullable = false)
    private PurchaseOrderItem purchaseOrderItem;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer quantityReceived;
}
