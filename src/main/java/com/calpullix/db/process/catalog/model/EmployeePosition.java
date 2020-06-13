package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum EmployeePosition {

	MANAGER(1, "Gerente"), DEPARTMENT_MANAGER(2, "Jefe de departamento"), CASHIER(3, "Cajero"), 
	JANITOR(4, "Auxiliar de intendencia"), ASSISTANT(5, "Ayudante general");
	
	private Integer idPosition;
	
	private String description;
	
	private EmployeePosition(int idPosition, String description) {
		this.idPosition = idPosition;
		this.description = description;
	}
	
	public static EmployeePosition of(int idPosition) {
		return Stream.of(EmployeePosition.values())
			.filter(p -> p.getIdPosition() == idPosition)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}
	
}
