package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name="payments")
@Getter @Setter @NoArgsConstructor
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="payment_id")
    private Long id;

    @NotNull @ManyToOne(optional=false)
    @JoinColumn(name="invoice_id", nullable=false)
    @JsonIgnoreProperties({"items"})
    private SalesPaymentReceipt invoice;

    @NotNull @PositiveOrZero @Column(nullable=false, precision=14, scale=2)
    private BigDecimal payment;

    @Column(name="payment_method")
    private String paymentMethod;

    @NotNull @PositiveOrZero @Column(nullable=false, precision=14, scale=2)
    private BigDecimal amount;
}
