package com.calpullix.db.process.branch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {

}
