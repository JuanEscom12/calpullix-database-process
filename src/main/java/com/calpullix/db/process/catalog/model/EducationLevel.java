package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum EducationLevel {
	
	BACHELOR_DEGREE(1, "Licenciatura"), HIGH_SCHOOL(2, "Bachillerato"), 
	BASIC(3, "Nivel básico");

	private Integer id;
	
	private String description;
	
	private EducationLevel(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static EducationLevel of(int id) {
		return Stream.of(EducationLevel.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	
}
