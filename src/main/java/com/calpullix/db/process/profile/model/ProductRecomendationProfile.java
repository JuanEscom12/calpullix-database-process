package com.calpullix.db.process.profile.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.product.model.Product;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ProductRecomendationProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idprofile", referencedColumnName = "id")
	private CustomerProfile idprofile;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idproduct", referencedColumnName = "id")
	private Product idproduct;
	
	private String date;
	
	private Boolean isactive;
	

}
