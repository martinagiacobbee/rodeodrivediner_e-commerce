package org.example.rodeodrivediner_webapp.repositories;

import org.example.rodeodrivediner_webapp.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}