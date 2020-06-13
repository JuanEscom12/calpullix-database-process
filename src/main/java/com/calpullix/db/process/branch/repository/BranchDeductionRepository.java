package com.calpullix.db.process.branch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.BranchDeduction;

@Repository
public interface BranchDeductionRepository extends JpaRepository<BranchDeduction, Integer> {

}
