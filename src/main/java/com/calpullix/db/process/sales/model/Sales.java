package com.calpullix.db.process.sales.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.customer.model.Customers;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.promotions.model.Promotions;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Sales {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idproduct", referencedColumnName = "id")
	private Product idproduct;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcustomer", referencedColumnName = "id")
	private Customers idcustomer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idbranch", referencedColumnName = "id")
	private Branch idbranch;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpromotion", referencedColumnName = "id")
	private Promotions idpromotion;
	
	private Integer quantity;
	

}
