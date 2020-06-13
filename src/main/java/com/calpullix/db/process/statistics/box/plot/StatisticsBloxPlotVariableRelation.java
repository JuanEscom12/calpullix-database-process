package com.calpullix.db.process.statistics.box.plot;

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
public class StatisticsBloxPlotVariableRelation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idstatisticsbloxplot", referencedColumnName = "id")
	private StatisticsBloxPlot idstatisticsbloxplot;

	private String valueVarInd;
	
	private String valueVarDep;
	
	private int orderGraphic;

}
