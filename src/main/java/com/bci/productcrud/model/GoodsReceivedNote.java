package com.bci.productcrud.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "goods_received_notes")
@Getter
@Setter
@NoArgsConstructor
public class GoodsReceivedNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String grnNumber;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @NotBlank
    @Column(nullable = false)
    private String receivedBy;

    @Column(nullable = false)
    private Instant receivedDate;

    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GrnStatus status;

    @NotEmpty
    @Valid
    @OneToMany(mappedBy = "grn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrnItem> items = new ArrayList<>();

    @Column(updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    public void addItem(GrnItem item) {
        items.add(item);
        item.setGrn(this);
    }

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
        if (receivedDate == null) {
            receivedDate = Instant.now();
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
