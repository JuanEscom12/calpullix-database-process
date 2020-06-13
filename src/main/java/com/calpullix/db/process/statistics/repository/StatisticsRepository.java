package com.calpullix.db.process.statistics.repository;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.statistics.model.Statistics;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {
	
	@Async
	CompletableFuture<Statistics> findOneByIdbranchAndIdproductAndYearAndMonth(Branch idbranch, Product idproduct, Integer year, Integer month);
	
	@Async
	CompletableFuture<Statistics> findOneByIdbranchAndYearAndMonth(Branch idbranch, Integer year, Integer month);

	@Async
	CompletableFuture<Statistics> findOneByIdbranchAndIdproductAndYear(Branch idbranch, Product idproduct, Integer year);
	
	@Async
	CompletableFuture<Statistics> findOneByIdbranchAndYear(Branch idbranch, Integer year);

}
