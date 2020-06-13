package com.calpullix.db.process.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class ProductPreferencesProfileRowMapper implements RowMapper<ProductPromotionsPreferencesProfile> {

	@Override
    public ProductPromotionsPreferencesProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
		final ProductPromotionsPreferencesProfile profilePreferences = new ProductPromotionsPreferencesProfile();
		profilePreferences.setIdProduct(rs.getInt("idProduct"));
		profilePreferences.setQuantityPurchases(rs.getInt("quantityPurchases")); 
        return profilePreferences;
    }
}
