package com.calpullix.db.process.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.product.model.DataSheet;

@Repository
public interface DataSheetRepository extends JpaRepository<DataSheet, Integer> {

}
