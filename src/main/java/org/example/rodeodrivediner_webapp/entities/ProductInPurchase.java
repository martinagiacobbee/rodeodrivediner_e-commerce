package org.example.rodeodrivediner_webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name="prod_in_purchase")
public class ProductInPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="prodInPurch_id")
    private int prodInPurchId;

    @Basic
    @Column(name = "quantity", nullable = true)
    private int quantity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "products")
    private Product prodotto;

    @ManyToOne
    @JoinColumn(name = "purchase")
    @ToString.Exclude
    @JsonIgnore
    private Purchase purchase;
}
