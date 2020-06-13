package com.calpullix.db.process.customer.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.customer.model.CustomerProfile;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Integer> {
	
	List<CustomerProfile> findAllByActive(Boolean active);

	List<CustomerProfile> findAllByActive(Boolean active, Pageable pagination);
	
	@Query("SELECT COUNT(c) FROM CustomerProfile c WHERE c.active = ?1 ")
	int getCountCustomerProfileByActive(Boolean active);
	
}
