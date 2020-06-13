package com.calpullix.db.process.statistics.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.statistics.model.StatisticsAnova;

@Repository
public interface StatisticsAnovaRepository extends JpaRepository<StatisticsAnova, Integer> {
	
	@Async
	CompletableFuture<List<StatisticsAnova>> findOneByIdbranchAndIdproductAndYearAndMonth(Branch idbranch, Product idproduct, Integer year, Integer month);

	@Async
	CompletableFuture<List<StatisticsAnova>> findOneByIdbranchAndYearAndMonth(Branch idbranch, Integer year, Integer month);
	
	@Async
	CompletableFuture<List<StatisticsAnova>> findOneByIdbranchAndYear(Branch idbranch, Integer year);

	@Async
	CompletableFuture<List<StatisticsAnova>> findOneByIdbranchAndIdproductAndYear(Branch idbranch, Product idproduct, Integer year);

}
