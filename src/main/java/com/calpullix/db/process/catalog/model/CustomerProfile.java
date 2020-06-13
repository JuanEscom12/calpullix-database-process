package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum CustomerProfile {

	LOW(1, "Minimo"), AVERAGE(4, "Promedio"), VIP(2, "VIP"), 
	PREMIUM(3, "Premium");
	
	private Integer id;
	
	private String description;
	
	private CustomerProfile(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static CustomerProfile of(int id) {
		return Stream.of(CustomerProfile.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

}
