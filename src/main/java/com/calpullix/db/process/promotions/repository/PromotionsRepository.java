package com.calpullix.db.process.promotions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.promotions.model.Promotions;

@Repository
public interface PromotionsRepository extends JpaRepository<Promotions, Integer> {

}
