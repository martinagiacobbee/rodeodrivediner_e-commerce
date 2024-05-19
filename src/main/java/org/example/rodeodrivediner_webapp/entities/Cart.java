package org.example.rodeodrivediner_webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cart")
@Data
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id",nullable = false)
    private Integer cartId;

    @OneToOne
    @JoinColumn(name="customer")
    private Customer customer;

    @OneToMany(targetEntity = ProductInCart.class, mappedBy = "cart",cascade = CascadeType.MERGE)
    @ToString.Exclude
    @JsonIgnore
    private List<ProductInCart> prodottiInCart=new ArrayList<>();

}
