package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum BranchRegion {

	REGION_NORTH(1, "Norte"), REGION_SOUTH(2, "Sur"), 
	REGION_WEST(3, "Oeste"), REGION_EAST(4, "Este");
	
	private Integer id;

	private String description;

	private BranchRegion(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public static BranchRegion of(int id) {
		return Stream.of(BranchRegion.values()).filter(p -> p.getId() == id).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
