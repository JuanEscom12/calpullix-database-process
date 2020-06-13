package com.calpullix.db.process.product.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.catalog.model.ProductStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ProductHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idproduct", referencedColumnName = "id")
	private Product idproduct;
	
	private String creationdate;
	
	@Basic
	@Column(name = "status")
	private Integer statusvalue;
	
	@Transient
	private ProductStatus status;
	
	private BigDecimal saleprice;
	
	private BigDecimal purchaseprice;
	
	private BigDecimal iva;
	
	@OneToMany(
	        mappedBy = "idproducthistory",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ProductBranch> productBranch;
	
	@PostLoad
    void fillTransient() {
        if (statusvalue > 0) {
            this.status = ProductStatus.of(statusvalue);
        }
       
    }
 
    @PrePersist
    void fillPersistent() {    
        if (BooleanUtils.negate(status == null)) {
            this.statusvalue = status.getId();
        }
    }

}
