package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum ProductClassification {
	
	BUSINESS_PREMIUM(1, "Premium"), BUSINESS_LOYALTY(2, "Loyalty"), 
	INTERMEDIATE(3, "Intermediate"), BASIC(4, "Basic");
	
	private Integer id;
	
	private String description;
	
	private ProductClassification(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static ProductClassification of(int id) {
		return Stream.of(ProductClassification.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}


}
