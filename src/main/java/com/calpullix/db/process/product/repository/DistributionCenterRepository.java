package com.calpullix.db.process.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.product.model.DistributionCenter;

@Repository
public interface DistributionCenterRepository extends JpaRepository<DistributionCenter, Integer> {

}
