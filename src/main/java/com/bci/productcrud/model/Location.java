package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="locations")
@Getter @Setter @NoArgsConstructor
public class Location {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="location_id")
    private Long id;

    @NotBlank @Column(name="location_name", nullable=false)
    private String locationName;

    private String address;

    @OneToMany(mappedBy="location")
    @JsonIgnore
    private List<Inventory> inventory = new ArrayList<>();
}
