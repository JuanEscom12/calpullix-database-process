package com.calpullix.db.process.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.sales.model.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {

}
