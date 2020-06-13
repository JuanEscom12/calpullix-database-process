package com.calpullix.db.process.statistics.repository;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.statistics.model.StatisticsCorrelation;

@Repository
public interface StatisticsCorrelationRepository extends JpaRepository<StatisticsCorrelation, Integer>  {
	
	@Async
	CompletableFuture<StatisticsCorrelation> findOneByIdbranchAndIdproductAndYearAndMonth(Branch idbranch, Product idproduct, Integer year, Integer month);

	@Async
	CompletableFuture<StatisticsCorrelation> findOneByIdbranchAndYearAndMonth(Branch idbranch, Integer year, Integer month);
	
	@Async
	CompletableFuture<StatisticsCorrelation> findOneByIdbranchAndYear(Branch idbranch, Integer year);

	@Async
	CompletableFuture<StatisticsCorrelation> findOneByIdbranchAndIdproductAndYear(Branch idbranch, Product idproduct, Integer year);

}
