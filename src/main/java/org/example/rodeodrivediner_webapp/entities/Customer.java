package org.example.rodeodrivediner_webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name="customer")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cust_id")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @OneToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name="cart")
    private Cart cart;

    @OneToMany(mappedBy="customer", cascade=CascadeType.MERGE)
    @JsonIgnore
    private List<Purchase> purchases;

    public Cart getCart() {
        return cart;
    }
}
