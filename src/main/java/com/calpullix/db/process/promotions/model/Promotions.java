package com.calpullix.db.process.promotions.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.catalog.model.PromotionsStatus;
import com.calpullix.db.process.customer.model.CustomersPromotions;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.product.model.ProductBranch;
import com.calpullix.db.process.profile.model.ProfilePromotions;
import com.calpullix.db.process.profile.model.PromotionsRecomendationProfile;
import com.calpullix.db.process.sales.model.Sales;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Promotions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	private String name;
	
	private String description;
	
	private String creationdate;
	
	private String enddate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idproduct", referencedColumnName = "id")
	private Product idproduct;
	
	private BigDecimal pricepromotion;
	
	private BigDecimal percentagediscount;
	
	private BigDecimal taxes;
	
	@Basic
	@Column(name = "status")
	private Integer statusvalue;
	
	@Transient
	private PromotionsStatus status;
	
	@Lob
	private byte[] image;
	
	
	@OneToMany(
	        mappedBy = "idpromotion",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<CustomersPromotions> customerpromotions;
	
	
	@OneToMany(
	        mappedBy = "idpromotion",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ProfilePromotions> profilepromotions;
	
	@OneToMany(
	        mappedBy = "idpromotion",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<PromotionsRecomendationProfile> promotionsrecomendationprofile;
	
	@OneToMany(
	        mappedBy = "idpromotion",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<Sales> sales;
	
	@OneToMany(
	        mappedBy = "idcustomer",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ProductBranch> productBranch;

	
	@PostLoad
    void fillTransient() {
        if (statusvalue > 0) {
            this.status = PromotionsStatus.of(statusvalue);
        }
    }
 
    @PrePersist
    void fillPersistent() {
        if (BooleanUtils.negate(status == null)) {
            this.statusvalue = status.getId();
        }
    }
	
	

}
