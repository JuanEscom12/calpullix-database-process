package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum Services {
	
	INTERNET(1, "Servicio de internet"), ELECTRICITY(2, "Electricidad"), 
	PHONE(3, "Teléfono Local"), WATER(4, "Agua"), GAS(5, "Gas L.P."), 
	SECURITY(6, "Servicio de seguridad"), MAINTENANCE(7, "Servicio de mantenimiento");
	
	private Integer id;
	
	private String description;
	
	private Services(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static Services of(int id) {
		return Stream.of(Services.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

}
