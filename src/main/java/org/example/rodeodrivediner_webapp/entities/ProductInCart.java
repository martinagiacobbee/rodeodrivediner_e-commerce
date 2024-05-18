package org.example.rodeodrivediner_webapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name="products_in_cart")
public class ProductInCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="prodInCart_id")
    private int id;

    @Basic
    @Column(name="quantity",nullable = true)
    private int quantity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="cart")
    private Cart cart;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="products")
    private Product products;

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof ProductInCart)) return false;
        ProductInCart c = (ProductInCart) o;
        return c.id==id;
    }
}
