package org.example.rodeodrivediner_webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="prod_id")
    private int prodId;

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="description")
    private String description;

    @NotNull
    @Column(name="price")
    private double price;

    @NotNull
    @Column(name="quantity")
    private int quantity;


    @OneToMany(targetEntity = ProductInCart.class, mappedBy="products",cascade= CascadeType.MERGE)
    @ToString.Exclude
    @JsonIgnore
    private List<ProductInCart> prodottoInCart;

}
