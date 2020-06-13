package com.calpullix.db.process.service.impl;

import lombok.Getter;

import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProductPromotionsPreferencesProfile {
	
	private Integer idCustomer;
	
	private Integer idProduct;
	
	private Integer idPromotion;
	
	private String name;
	
	private Integer quantityPurchases;

}
