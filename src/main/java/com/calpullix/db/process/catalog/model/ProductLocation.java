package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum ProductLocation {

	SHELF(1, "Anaquel"), CELLAR(2, "Bodega"), SOLD(3, "Vendido"), 
	PROVIDER(4, "Con proveedor"), LOST(5, "Extraviado");
	 
	private Integer id;
	
	private String description;
	
	private ProductLocation(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static ProductLocation of(int id) {
		return Stream.of(ProductLocation.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

}
