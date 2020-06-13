package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum State {
	
	Aguascalientes(1, "Aguascalientes"),
	Baja_California(2, "Baja California"),
	Baja_California_Sur(3, "Baja California Sur"),
	Campeche(4, "Campeche"),
	Chiapas(5, "Chiapas"),
	Chihuahua(6, "Chihuahua"),
	Coahuila(7, "Coahuila"),
	Colima(8, "Colima"),
	Ciudad_Mexico(9, "CDMX"),
	Durango(10, "Durango"),
	Estado_Mexico(11, "Estado de México"),
	Guanajuato(12, "Guanajuato"),
	Guerrero(13, "Guerrero"),
	Hidalgo(14, "Hidalgo"),
	Jalisco(15, "Jalisco"),
	Michoacan(16, "Michoacán"),
	Morelos(17, "Morelos"),
	Nayarit(18, "Nayarit"),
	Nuevo_Leon(19, "Nuevo León"),
	Oaxaca(20, "Oaxaca"),
	Puebla(21, "Puebla"),
	Queretaro(22, "Querétaro"),
	Quintana_Roo(23, "Quintana Roo"),
	San_Luis_Potosi(24, "San Luis Potosí"),
	Sinaloa(25, "Sinaloa"),
	Sonora(26, "Sonora"),
	Tabasco(27, "Tabasco"),
	Tamaulipas(28, "Tamaulipas"),
	Tlaxcala(29, "Tlaxcala"),
	Veracruz(30, "Veracruz"),
	Yucatán(31, "Yucatán"),
	Zacatecas(32, "Zacatecas");
	
	private Integer id;
	
	private String description;
	
	private State(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static State of(int id) {
		return Stream.of(State.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

}
