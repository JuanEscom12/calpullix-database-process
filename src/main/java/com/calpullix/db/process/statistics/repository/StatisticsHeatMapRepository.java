package com.calpullix.db.process.statistics.repository;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.statistics.model.StatisticsHeatmap;

@Repository
public interface StatisticsHeatMapRepository extends JpaRepository<StatisticsHeatmap, Integer> {

	@Async
	CompletableFuture<StatisticsHeatmap> findOneByIdbranchAndIdproductAndYearAndMonth(Branch idbranch, Product idproduct, Integer year, Integer month);

	@Async
	CompletableFuture<StatisticsHeatmap> findOneByIdbranchAndYear(Branch idbranch, Integer year);
	
	@Async
	CompletableFuture<StatisticsHeatmap> findOneByIdbranchAndYearAndMonth(Branch idbranch, Integer year, Integer month);

	@Async
	CompletableFuture<StatisticsHeatmap> findOneByIdbranchAndIdproductAndYear(Branch idbranch, Product idproduct, Integer year);

	
}
