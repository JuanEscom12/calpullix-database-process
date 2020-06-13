package com.calpullix.db.process.catalog.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum ProductCategories {
	
	BODY_CLEANSING(1, "Limpieza-Corporal"), 
	PERSONAL_CARE(2, "Cuidado-Personal"),
	CREAMS_COSMETIC(3, "Cremas-Cosméticos"), //
	DERMATOLOGICAL_ITEMS(4 , "Artículos-Dermatológicos"), //
	GROCERIES(5, "Abarrotes"),
	HOME_CLEAN(6, "Limpieza-Hogar"), 
	DAIRY_PRODUCTS(7, "Lácteos"), 
	CEREALS(8, "Cereales"), 
	BUTCHER(9, "Carnicería"),
	MISCELLANY(10, "Miscelánea"),
	CANNED_PACKED(11, "Envasados");
	
	private Integer id;
	
	private String description;
	
	private ProductCategories(int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static ProductCategories of(int id) {
		return Stream.of(ProductCategories.values())
			.filter(p -> p.getId() == id)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}


}
