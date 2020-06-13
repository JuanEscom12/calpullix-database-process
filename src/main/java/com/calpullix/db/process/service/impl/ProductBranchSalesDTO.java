package com.calpullix.db.process.service.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProductBranchSalesDTO {
	
	private String year;
	
	private String month;

	private Integer sales;
	
}
