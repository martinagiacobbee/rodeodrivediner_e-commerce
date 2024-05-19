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

    @Query(value = "SELECT * "+
            "FROM products pr "+
            "WHERE pr.name LIKE :name OR :name IS NULL",nativeQuery = true)
    List<Product> findByName(String nome);

}
