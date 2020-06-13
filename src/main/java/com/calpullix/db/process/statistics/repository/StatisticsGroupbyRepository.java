package com.calpullix.db.process.statistics.repository;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.statistics.model.StatisticsGroupby;

@Repository
public interface StatisticsGroupbyRepository extends JpaRepository<StatisticsGroupby, Integer> {

	@Async
	CompletableFuture<StatisticsGroupby> findOneByIdbranchAndIdproductAndYearAndMonth(Branch idbranch, Product idproduct, Integer year, Integer month);

	@Async
	CompletableFuture<StatisticsGroupby> findOneByIdbranchAndYearAndMonth(Branch idbranch, Integer year, Integer month);
	
	@Async
	CompletableFuture<StatisticsGroupby> findOneByIdbranchAndYear(Branch idbranch, Integer year);

	@Async
	CompletableFuture<StatisticsGroupby> findOneByIdbranchAndIdproductAndYear(Branch idbranch, Product idproduct, Integer year);

}
