package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum ContactType {
	
	CELL_PHONE(1, "Celular"), EMAIL(2, "Email"), FACEBOOK(3, "Facebook"), 
	INSTAGRAM(4, "Instagram"), PHONE(5, "Teléfono");
	
	private Integer id;
	
	private String description;
	
	private ContactType(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static ContactType of(int id) {
		return Stream.of(ContactType.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

}
