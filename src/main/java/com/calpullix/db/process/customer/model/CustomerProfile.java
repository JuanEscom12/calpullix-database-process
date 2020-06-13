package com.calpullix.db.process.customer.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.profile.model.ProductRecomendationProfile;
import com.calpullix.db.process.profile.model.ProfileKmeans;
import com.calpullix.db.process.profile.model.ProfileKnearest;
import com.calpullix.db.process.profile.model.ProfilePromotions;
import com.calpullix.db.process.profile.model.ProfileRegression;
import com.calpullix.db.process.profile.model.PromotionsRecomendationProfile;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class CustomerProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	private String name;
	
	private String description;
	
	private String creationdate;
	
	private Boolean active;
	
	@OneToMany(
	        mappedBy = "idprofile",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ProfilePromotions> profilePromotions;
	
	@OneToMany(
	        mappedBy = "idprofile",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<Customers> customers;

	@OneToMany(
	        mappedBy = "idprofile",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ProfileKmeans> profileKmeans;
	
	@OneToMany(
	        mappedBy = "idprofile",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ProfileKnearest> profileKnearest;

	@OneToMany(
	        mappedBy = "idprofile",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ProductRecomendationProfile> productRecomendationProfile;
	
	@OneToMany(
	        mappedBy = "idprofile",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<PromotionsRecomendationProfile> promotionsRecomendationProfile;

	@OneToMany(
	        mappedBy = "idprofile",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ProfileRegression> profileRegression;
	
}
