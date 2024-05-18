package org.example.rodeodrivediner_webapp.repositories;

import org.example.rodeodrivediner_webapp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
