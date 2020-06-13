package com.calpullix.db.process.product.model;

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
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.catalog.model.ProductBranchStatus;
import com.calpullix.db.process.catalog.model.ProductLocation;
import com.calpullix.db.process.customer.model.Customers;
import com.calpullix.db.process.promotions.model.Promotions;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ProductBranch {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idbranch", referencedColumnName = "id")	
	private Branch idbranch;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idproducthistory", referencedColumnName = "id")	
	private ProductHistory idproducthistory;
	
	@Basic
	@Column(name = "status")
	private Integer statusvalue;
	
	@Transient
	private ProductBranchStatus status;
	
	private String creationdate;
	
	private String saledate;
	
	private String expirationdate;
	
	@Lob
	private byte[] barcode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcustomer", referencedColumnName = "id")
	private Customers idcustomer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpromotion", referencedColumnName = "id")
	private Promotions idpromotion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "distributioncenter", referencedColumnName = "id")
	private DistributionCenter distributioncenter;
	
	@Basic
	@Column(name = "location")
	private Integer locationvalue;
	
	@Transient
	private ProductLocation location;
	
	
	@PostLoad
    void fillTransient() {
        if (statusvalue > 0) {
            this.status = ProductBranchStatus.of(statusvalue);
        }
        if (locationvalue > 0) {
            this.location = ProductLocation.of(locationvalue);
        }
    }
 
    @PrePersist
    void fillPersistent() {
        if (BooleanUtils.negate(status == null)) {
            this.statusvalue = status.getId();
        }
        if (BooleanUtils.negate(location == null)) {
            this.locationvalue = location.getId();
        }
    }
}
