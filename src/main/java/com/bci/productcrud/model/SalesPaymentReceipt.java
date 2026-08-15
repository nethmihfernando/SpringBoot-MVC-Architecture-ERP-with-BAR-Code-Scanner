package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="sales_payment_receipts")
@Getter @Setter @NoArgsConstructor
public class SalesPaymentReceipt {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="receipt_id")
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="cashier_id", nullable=false)
    @JsonIgnoreProperties({"role"})
    private User cashier;

    @NotNull @PositiveOrZero @Column(name="total_amount", nullable=false, precision=14, scale=2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @PositiveOrZero @Column(precision=14, scale=2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name="payment_method")
    private String paymentMethod;

    private String status = "PAID";

    @OneToMany(mappedBy="receipt", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<SalesReceiptItem> items = new ArrayList<>();

    public void addItem(SalesReceiptItem item) {
        items.add(item);
        item.setReceipt(this);
    }

    public void recalculateTotal() {
        BigDecimal gross = items.stream().map(SalesReceiptItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalAmount = gross.subtract(discount == null ? BigDecimal.ZERO : discount);
    }

    @PrePersist @PreUpdate void calculate() { recalculateTotal(); }
}
