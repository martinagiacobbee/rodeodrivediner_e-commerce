package org.example.rodeodrivediner_webapp.repositories;

import org.example.rodeodrivediner_webapp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByNameContainingIgnoreCase(String productName, Pageable pageable); //ignoreCase lets the query be case-insensitive

    Product findByProdId(int id);

    boolean existsByProdId(int id);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE (p.name LIKE ?1 OR ?1 IS NULL) "
            )
    List<Product> findByName(String name);

}
