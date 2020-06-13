package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum MaritalStatus {

	SINGLE(1, "Soltero"), MARRIED(2, "Casado"), 
	WIDOWER(3, "Viudo"), DIVORCED(4, "Divorciado");
	
	private Integer id;
	
	private String description;
	
	private MaritalStatus(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static MaritalStatus of(int id) {
		return Stream.of(MaritalStatus.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	
}
