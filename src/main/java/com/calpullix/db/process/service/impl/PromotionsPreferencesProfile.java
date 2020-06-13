package com.calpullix.db.process.service.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class PromotionsPreferencesProfile {
	
	private Integer idCustomer;
	
	private Integer idPromotion;
	
	private String namePromotion;
	
	private Integer quantityPurchases;

}
