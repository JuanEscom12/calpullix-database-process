package com.calpullix.db.process.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.profile.model.PromotionsRecomendationProfile;

@Repository
public interface PromotionsRecomendationProfileRepository extends JpaRepository<PromotionsRecomendationProfile, Integer> {

}
