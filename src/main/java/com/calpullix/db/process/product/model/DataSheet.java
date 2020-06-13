package com.calpullix.db.process.product.model;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.catalog.model.WeightUnit;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class DataSheet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idproduct", referencedColumnName = "id")	
	private Product idproduct;
	
	private String component;
	
	private BigDecimal weight;
	
	@Basic
	@Column(name = "weightunit")
	private Integer weightunitvalue;
	
	@Transient
	private WeightUnit weightunit;
	
	@PostLoad
    void fillTransient() {
        if (weightunitvalue > 0) {
            this.weightunit = WeightUnit.of(weightunitvalue);
        }
        
    }
 
    @PrePersist
    void fillPersistent() {
        if (BooleanUtils.negate(weightunit == null)) {
            this.weightunitvalue = weightunit.getId();
        }
        
    }
	
}
