package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @NotBlank @Column(name = "product_name")
    private String name;

    private String category;
    private String brand;
    private String size;
    private String color;

    @NotBlank @Column(nullable = false, unique = true)
    private String barcode;

    @NotNull @PositiveOrZero @Column(name = "purchase_price", precision = 14, scale = 2)
    private BigDecimal purchasePrice = BigDecimal.ZERO;

    @NotNull @PositiveOrZero @Column(name = "selling_price", precision = 14, scale = 2)
    private BigDecimal sellingPrice = BigDecimal.ZERO;

    private String status = "ACTIVE";

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Inventory> inventory = new ArrayList<>();
}
