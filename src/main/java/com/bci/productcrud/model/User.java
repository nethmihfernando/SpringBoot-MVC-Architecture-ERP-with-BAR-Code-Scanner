package com.bci.productcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter @Setter @NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @NotBlank @Column(name="full_name", nullable=false)
    private String fullName;

    @NotBlank @Column(nullable=false, unique=true)
    private String username;

    @NotBlank @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable=false)
    private String password;

    @ManyToOne(optional=false)
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    @Email private String email;

    @Column(name="contact_no")
    private String contactNo;

    private String status = "ACTIVE";
}
