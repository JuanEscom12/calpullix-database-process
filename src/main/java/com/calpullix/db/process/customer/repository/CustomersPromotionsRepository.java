package com.calpullix.db.process.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.customer.model.CustomersPromotions;

@Repository
public interface CustomersPromotionsRepository extends JpaRepository<CustomersPromotions, Integer> {

}
