package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum TwitterTypeMessage {

	POSITIVE(1, "Positivo"), NEGATIVE(2, "Negativo"), NEUTRAL(3, "Neutral");
	
	private Integer id;
	
	private String description;
	
	private TwitterTypeMessage(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static TwitterTypeMessage of(int id) {
		return Stream.of(TwitterTypeMessage.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}
	
}
