package com.calpullix.db.process.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.profile.model.ProfilePromotions;
import com.calpullix.db.process.promotions.model.Promotions;

@Repository
public interface ProfilePromotionsRepository extends JpaRepository<ProfilePromotions, Integer> {
	
	Page<ProfilePromotions> findAllByIdprofileAndActive(CustomerProfile idprofile, Boolean active, Pageable pagination);
	
	@Query("SELECT COUNT(p) FROM ProfilePromotions p WHERE p.idprofile = ?1 AND p.active = ?2 ")
	int getCountProfilePromotionsByIdProfileAndActive(CustomerProfile idprofile, Boolean active);
	
	List<ProfilePromotions> findByIdpromotionAndIdprofileAndActive(Promotions idpromotion, CustomerProfile idprofile, Boolean active);
	
	Optional<List<ProfilePromotions>> findAllByIdprofileAndActive(CustomerProfile idprofile, Boolean isactive);
	
	
}
