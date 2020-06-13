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
public class StatisticsVariableRelation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	private String statisticvar;

	private String varvalueone;
	
	private String varvaluetwo;
	
	private String varvaluethree;
	
	private String varvaluefour;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idstatistics", referencedColumnName = "id")
	private Statistics idstatistics;
	
	
}
