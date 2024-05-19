package org.example.rodeodrivediner_webapp.repositories;

import org.example.rodeodrivediner_webapp.entities.ProductInPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInPurchaseRepository extends JpaRepository<ProductInPurchase, Integer> {
}
