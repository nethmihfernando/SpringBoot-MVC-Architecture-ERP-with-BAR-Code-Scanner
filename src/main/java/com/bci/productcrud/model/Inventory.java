package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name="inventory", uniqueConstraints=@UniqueConstraint(name="uk_inventory_product_location", columnNames={"product_id","location_id"}))
@Getter @Setter @NoArgsConstructor
public class Inventory {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="inventory_id")
    private Long id;

    @NotNull @ManyToOne(optional=false)
    @JoinColumn(name="product_id", nullable=false)
    @JsonIgnoreProperties({"inventory"})
    private Product product;

    @NotNull @ManyToOne(optional=false)
    @JoinColumn(name="location_id", nullable=false)
    @JsonIgnoreProperties({"inventory"})
    private Location location;

    @NotNull @PositiveOrZero @Column(name="quantity_on_hand", nullable=false)
    private Integer quantityOnHand = 0;

    @Column(name="last_updated", nullable=false)
    private Instant lastUpdated;

    @PrePersist @PreUpdate
    void touch() { lastUpdated = Instant.now(); }
}
