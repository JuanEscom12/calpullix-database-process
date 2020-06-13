package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum PromotionsStatus {

	ACTIVE(1, "Activa"), INACTIVE(2, "Inactiva");
	
	private Integer id;
	
	private String description;
	
	private PromotionsStatus(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static PromotionsStatus of(int id) {
		return Stream.of(PromotionsStatus.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	
}
