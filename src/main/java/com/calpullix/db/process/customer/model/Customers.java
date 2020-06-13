package com.calpullix.db.process.customer.model;

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

import com.calpullix.db.process.catalog.model.EducationLevel;
import com.calpullix.db.process.catalog.model.MaritalStatus;
import com.calpullix.db.process.catalog.model.State;
import com.calpullix.db.process.product.model.ProductBranch;
import com.calpullix.db.process.profile.model.ProfileRegression;
import com.calpullix.db.process.sales.model.Sales;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Customers {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	private String name;
	
	private Integer age;
	
	@Basic
	@Column(name = "state")
	private Integer statevalue;
	
	@Transient
	private State state;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idprofile", referencedColumnName = "id")
	private CustomerProfile idprofile;
	
	private String municipality;
	
	private String sex;
	
	@Basic
	@Column(name = "educationlevel")
	private Integer educationlevelvalue;
	
	@Transient
	private EducationLevel educationlevel;
	
	private String job;
	
	@Basic
	@Column(name = "maritalstatus")
	private Integer maritalstatusvalue;
	
	@Transient
	private MaritalStatus maritalstatus;
	
	private BigDecimal income;
	
	private BigDecimal debt;
	
	private Integer classification;
	
	@OneToMany(
	        mappedBy = "idcustomer",
	        orphanRemoval = false,
	        fetch = FetchType.LAZY)
	private List<CustomersPromotions> customerpromotions;
	
	@OneToMany(
	        mappedBy = "idcustomer",
	        orphanRemoval = false,
	        fetch = FetchType.LAZY)
	private List<ProductBranch> productBranch;

	@OneToMany(
	        mappedBy = "idcustomer",
	        orphanRemoval = false,
	        fetch = FetchType.LAZY)
	private List<Sales> sale;
	
	@OneToMany(
	        mappedBy = "initialcustomer",
	        orphanRemoval = false,
	        fetch = FetchType.LAZY)
	private List<ProfileRegression> profileRegression;
	
	@PostLoad
    void fillTransient() {
        if (statevalue > 0) {
            this.state = State.of(statevalue);
        }
        if (educationlevelvalue > 0) {
            this.educationlevel = EducationLevel.of(educationlevelvalue);
        }
        if (maritalstatusvalue > 0) {
            this.maritalstatus = MaritalStatus.of(maritalstatusvalue);
        }
    }
 
    @PrePersist
    void fillPersistent() {
        if (BooleanUtils.negate(state == null)) {
            this.statevalue = state.getId();
        }
        if (BooleanUtils.negate(educationlevel == null)) {
            this.educationlevelvalue = educationlevel.getId();
        }
        if (BooleanUtils.negate(maritalstatus == null)) {
            this.maritalstatusvalue = maritalstatus.getId();
        }
    }
	
	
}
