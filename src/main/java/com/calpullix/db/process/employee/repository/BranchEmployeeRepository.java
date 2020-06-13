package com.calpullix.db.process.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.employee.model.BranchEmployee;

@Repository
public interface BranchEmployeeRepository extends JpaRepository<BranchEmployee, Integer> {

}
