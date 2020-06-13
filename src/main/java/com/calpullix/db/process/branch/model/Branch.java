package com.calpullix.db.process.branch.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.catalog.model.BranchRegion;
import com.calpullix.db.process.catalog.model.ContactType;
import com.calpullix.db.process.catalog.model.State;
import com.calpullix.db.process.employee.model.Employee;
import com.calpullix.db.process.product.model.ProductBranch;
import com.calpullix.db.process.purchaseorder.model.Purchaseorder;
import com.calpullix.db.process.regression.model.ForecastSales;
import com.calpullix.db.process.sales.model.Sales;
import com.calpullix.db.process.statistics.box.plot.StatisticsBloxPlot;
import com.calpullix.db.process.statistics.model.Statistics;
import com.calpullix.db.process.statistics.model.StatisticsAnova;
import com.calpullix.db.process.statistics.model.StatisticsCorrelation;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Branch {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	private String name;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager", referencedColumnName = "id")
	private Employee manager;
	
	private String address;
	
	private String contact;
	
	@Basic
	@Column(name = "contacttype")
	private Integer contacttypevalue;
	
	@Transient
	private ContactType contactType;
	
	@Basic
	@Column(name = "state")
	private Integer statevalue;
	
	@Transient
	private State state;
	
	private String municipality;
	
	@Column(name = "postalcode")
	private Integer postalcode;
	
	@Basic
	@Column(name = "region")
	private Integer regionvalue;
	
	@Transient
	private BranchRegion region;
	
	@Column(name = "numberemployees")
	private Integer numberemployees;
	
	@Column(name = "monthlyrent")
	private BigDecimal monthlyrent;
	
	@Column(name = "monthlydeductions")
	private BigDecimal monthlydeductions;
	
	private String longitude;
	
	private String latitude;
	
	@Lob
	private byte[] image;
	
	@OneToMany(
		        mappedBy = "idbranch",
		        orphanRemoval = true,
		        fetch = FetchType.LAZY)
	private List<BranchServices> branchServices;
	
	@OneToMany(
	        mappedBy = "idbranch",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<BranchDeduction> branchDeduction;
	
	@OneToMany(
	        mappedBy = "idbranch",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ProductBranch> productBranch;
	
	@OneToMany(
	        mappedBy = "idbranch",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<Purchaseorder> purchaseOrder;
	
	@OneToMany(
	        mappedBy = "idbranch",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<StatisticsCorrelation> statisticsCorrelation;
	
	
	@OneToMany(
	        mappedBy = "idbranch",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<Sales> sales;
	
	@OneToMany(
	        mappedBy = "idbranch",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<Statistics> statistics;
	
	@OneToMany(
	        mappedBy = "idbranch",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<StatisticsAnova> statisticsanova;
	
	@OneToMany(
	        mappedBy = "idbranch",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)	
	private List<ForecastSales> forecastsales;
	
	@OneToMany(
	        mappedBy = "idbranch",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)	
	private List<StatisticsBloxPlot> statisticsBloxPlot;
	
	
	@PostLoad
    void fillTransient() {
        if (contacttypevalue > 0) {
            this.contactType = ContactType.of(contacttypevalue);
        }
        if (regionvalue > 0) {
            this.region = BranchRegion.of(regionvalue);
        }
        if (statevalue > 0) {
            this.state = State.of(statevalue);
        }
    }
 
    @PrePersist
    void fillPersistent() {
        if (BooleanUtils.negate(contactType == null)) {
            this.contacttypevalue = contactType.getId();
        }
        if (BooleanUtils.negate(region == null)) {
            this.regionvalue = region.getId();
        }
        if (BooleanUtils.negate(state == null)) {
            this.statevalue = state.getId();
        }
    }
	
	
}
