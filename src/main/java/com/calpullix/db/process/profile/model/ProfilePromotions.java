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
import com.calpullix.db.process.promotions.model.Promotions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class ProfilePromotions {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idprofile", referencedColumnName = "id")
	private CustomerProfile idprofile;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpromotion", referencedColumnName = "id")
	private Promotions idpromotion;
	
	private String creationdate;
	
	private Boolean accepted;
	
	private Boolean active;
	
}
