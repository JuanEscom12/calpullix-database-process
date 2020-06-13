package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum ProductStatus {

	ACTIVE(1, "Activo"), INACTIVE(2, "Inactivo");
	
	private Integer id;
	
	private String description;
	
	private ProductStatus(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static ProductStatus of(int id) {
		return Stream.of(ProductStatus.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}
	
}
