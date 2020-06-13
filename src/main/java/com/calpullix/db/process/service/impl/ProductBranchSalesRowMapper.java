package com.calpullix.db.process.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class ProductBranchSalesRowMapper implements RowMapper<ProductBranchSalesDTO> {

	@Override
    public ProductBranchSalesDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		final ProductBranchSalesDTO result = new ProductBranchSalesDTO();
		result.setMonth(rs.getString("month"));
		result.setYear(rs.getString("year"));
		result.setSales(rs.getInt("sales"));
        return result;
    }
	
}
