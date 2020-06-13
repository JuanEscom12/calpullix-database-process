package com.calpullix.db.process.regression.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ForecastSales {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idbranch", referencedColumnName = "id")
	private Branch idbranch;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idproduct", referencedColumnName = "id")
	private Product idproduct;
	
	private Integer year;
	
	private BigDecimal rmseprediction;
	
	private BigDecimal rmsetraining;
	
	private String bestarima;
	
	private Boolean isactive;

	@Lob
	private byte[] imageautocorrelation;
	
	@Lob
	private byte[] imagehistogram;
	
	@Lob
	private byte[] imageplot;
	
	@Lob
	private byte[] imageresults;
	
	@Lob
	private byte[] imagestationary;
		
	
	@OneToMany(
	        mappedBy = "idforecastsales",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ForecastVariables> forecastVariables;

}
