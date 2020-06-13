package com.calpullix.db.process.regression.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.regression.model.ForecastSales;

@Repository
public interface ForecastSalesRepository extends JpaRepository<ForecastSales, Integer> {

	Optional<ForecastSales> findOneByIdbranchAndIdproductAndYearAndIsactive(Branch idbranch, Product idproduct, Integer year, Boolean isactive);
	
	
	
	
}
