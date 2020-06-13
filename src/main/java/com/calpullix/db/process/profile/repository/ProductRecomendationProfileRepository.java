package com.calpullix.db.process.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.profile.model.ProductRecomendationProfile;

public interface ProductRecomendationProfileRepository extends JpaRepository<ProductRecomendationProfile, Integer> {
	
	@Query("SELECT COUNT(p) FROM ProductRecomendationProfile p WHERE p.idprofile = ?1 AND p.isactive = ?2")
	int getCountByIdprofileAndIsactive(CustomerProfile idprofile, Boolean isactive);
	
	Optional<List<ProductRecomendationProfile>> findAllByIdprofileAndIsactive(CustomerProfile idprofile, Boolean isactive, Pageable pagination);

	Optional<List<ProductRecomendationProfile>> findAllByIdprofileAndIsactive(CustomerProfile idprofile, Boolean isactive);
}
