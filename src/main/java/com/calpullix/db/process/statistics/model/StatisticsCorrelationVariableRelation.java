package com.calpullix.db.process.statistics.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class StatisticsCorrelationVariableRelation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	private String relationvalueone;
	
	private String relationvaluetwo;
	
	private String relationvaluethree;
	
	private String relationvaluefour;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idstatisticscorrelation", referencedColumnName = "id")
	private StatisticsCorrelation idstatisticscorrelation;
	
}
