package com.calpullix.db.process.branch.model;

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

import com.calpullix.db.process.catalog.model.Services;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class BranchServices {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	private String name;
	
	@Basic
	@Column(name = "idservice")
	private Integer idservicevalue;
	
	
	@Transient
	private Services idservice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idbranch", referencedColumnName = "id")
	private Branch idbranch;
	
	private BigDecimal monthlyamount;
	
	@PostLoad
    void fillTransient() {
        if (idservicevalue > 0) {
            this.idservice = Services.of(idservicevalue);
        }
        
    }
 
    @PrePersist
    void fillPersistent() {
        if (BooleanUtils.negate(idservice == null)) {
            this.idservicevalue = idservice.getId();
        }
        
    }
	
	
}
