package org.example.rodeodrivediner_webapp.repositories;

import org.example.rodeodrivediner_webapp.entities.ProductInCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInCartRepository extends JpaRepository<ProductInCart, Long> {
}
