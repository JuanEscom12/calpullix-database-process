package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum ProductBranchStatus {

	ON_SALE(1, "En venta"), SOLD(2, "Vendido"), 
	LOST(3, "Perdido"), CADUCOUS(4, "Caduco");
	
	private Integer id;
	
	private String description;
	
	private ProductBranchStatus(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static ProductBranchStatus of(int id) {
		return Stream.of(ProductBranchStatus.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	
}
