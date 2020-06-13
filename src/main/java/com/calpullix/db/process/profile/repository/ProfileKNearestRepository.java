package com.calpullix.db.process.profile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.profile.model.ProfileKnearest;

@Repository
public interface ProfileKNearestRepository extends JpaRepository<ProfileKnearest, Integer> {

	ProfileKnearest findOneByIdprofileAndIsactive(CustomerProfile idprofile, Boolean isactive);
	
	List<ProfileKnearest> findAllByIsactive(Boolean isactive);
	
}
