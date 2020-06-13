package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum WeightUnit {
	
	GR(1, "gramos"), ML(2, "mililitros"), CM(3, "centímetros");
	
	private Integer id;
	
	private String description;
	
	private WeightUnit(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static WeightUnit of(int id) {
		return Stream.of(WeightUnit.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}


}
