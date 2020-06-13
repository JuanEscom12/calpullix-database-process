package com.calpullix.db.process.statistics.box.plot.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.statistics.box.plot.StatisticsBloxPlot;

public interface StatisticsBloxPlotRepository extends JpaRepository<StatisticsBloxPlot, Integer> {
	
	@Async
	CompletableFuture<List<StatisticsBloxPlot>> findAllByIdbranchAndIdproductAndYearAndMonth(Branch idbranch, Product idproduct, Integer year, Integer month);
	
	@Async
	CompletableFuture<List<StatisticsBloxPlot>> findAllByIdbranchAndYearAndMonth(Branch idbranch, Integer year, Integer month);

	@Async
	CompletableFuture<List<StatisticsBloxPlot>> findAllByIdbranchAndYear(Branch idbranch, Integer year);

	@Async
	CompletableFuture<List<StatisticsBloxPlot>> findAllByIdbranchAndIdproductAndYear(Branch idbranch, Product idproduct, Integer year);

}
