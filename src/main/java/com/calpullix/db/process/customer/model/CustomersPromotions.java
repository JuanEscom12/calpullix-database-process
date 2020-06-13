package com.calpullix.db.process.customer.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.promotions.model.Promotions;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class CustomersPromotions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpromotion", referencedColumnName = "id")
	private Promotions idpromotion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcustomer", referencedColumnName = "id")
	private Customers idcustomer;
	
	private String date;
	
	private Integer quantity;
	
}
