package org.example.rodeodrivediner_webapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="purchase")
@Data
@ToString
public class Purchase {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="purchase_id")
    private int purchId;

    @ManyToOne
    @JoinColumn(name="customer")
    @ToString.Exclude
    private Customer customer;

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_time")
    private Date purchaseTime;

    @OneToMany(mappedBy="purchase",cascade=CascadeType.MERGE)
    private List<ProductInPurchase> productsInPurchase=new ArrayList<>();


    @Version
    private long version;
}
