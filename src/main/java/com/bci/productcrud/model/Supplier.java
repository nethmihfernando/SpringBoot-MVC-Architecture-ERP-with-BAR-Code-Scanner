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
@Table(name="suppliers")
@Getter @Setter @NoArgsConstructor
public class Supplier {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="supplier_id")
    private Long id;

    @NotBlank @Column(name="supplier_name", nullable=false)
    private String supplierName;

    @Column(name="contact_person") private String contactPerson;
    private String phone;
    private String address;
    private String email;
    @Column(name="bank_details") private String bankDetails;
    private String status = "ACTIVE";

    @OneToMany(mappedBy="supplier")
    @JsonIgnore
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();
}
