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
@Table(name="roles")
@Getter @Setter @NoArgsConstructor
public class Role {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="role_id")
    private Long id;

    @NotBlank @Column(nullable=false, unique=true)
    private String name;

    @OneToMany(mappedBy="role")
    @JsonIgnore
    private List<User> users = new ArrayList<>();
}
