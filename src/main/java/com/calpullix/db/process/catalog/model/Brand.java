package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum Brand {

	PROCTER(1, "Procter and Gambler"), GARNIER(2, "Garnier"), COLGATE_PALMOLIVE(3, "Colgate"), 
	KYMBERLY_CLARK(4, "Kimberly Clark"), UNILEVER(5, "Unilever"), PINSA(6, "Pinsa"),
	NESTLE(7, "Nestle"), CORONADO(8, "Coronado"), NO_BRAND(9, "Sin marca"), 
	LA_COSTENA(10, "La Costeña"), GAMESA(11, "Gamesa"), MCCORMICK(12, "Mc Cormick"), 
	ZOTE(13, "Zote"), GUILLETE(14, "Guillete"), LALA(15, "Lala"), VOLCANES(16, "Los Volcanes"), FUD(17, "FUD"),
	DURACELL(18, "Duracell");
	
	private Integer id;
	
	private String description;
	
	private Brand(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static Brand of(int id) {
		return Stream.of(Brand.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

}
