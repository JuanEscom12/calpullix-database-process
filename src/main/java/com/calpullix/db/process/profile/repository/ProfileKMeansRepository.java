package com.calpullix.db.process.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.profile.model.ProfileKmeans;

@Repository
public interface ProfileKMeansRepository extends JpaRepository<ProfileKmeans, Integer> {
	
	ProfileKmeans findOneByIdprofileAndIsactive(CustomerProfile idprofile, Boolean isactive);
	
}
