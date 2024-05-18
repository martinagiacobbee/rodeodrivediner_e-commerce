package org.example.rodeodrivediner_webapp.repositories;

import org.example.rodeodrivediner_webapp.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
