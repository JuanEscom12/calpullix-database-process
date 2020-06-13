package com.calpullix.db.process.statistics.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class StatisticsGroupby {
	
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
	
	private Integer month;
	
	private String horizontalvarnameone;

	private String horizontalvarnametwo;
	
	private String horizontalvarnamethree;
	
	private String horizontalvarnamefour;
	
	@OneToMany(
	        mappedBy = "idstatisticsgroupby",
	        orphanRemoval = true,
	        fetch = FetchType.EAGER)
	private List<StatisticsGroupbyVariableRelation> statisticsGroupbyVariableRelation;

	
}
