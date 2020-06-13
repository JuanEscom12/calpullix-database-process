package com.calpullix.db.process.product.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query("SELECT pb.idproducthistory.idproduct FROM ProductBranch pb WHERE pb.saledate BETWEEN ?1 AND ?2 AND pb.idbranch  = ?3 AND pb.statusvalue = ?4 "
			+ "GROUP BY pb.idproducthistory.idproduct ORDER BY SUM(pb.idproducthistory.saleprice) DESC")
	@Async
	CompletableFuture<List<Product>> getProductTopFiveBySaledateAndIdbranchAndStatus(String initdate, String enddate, Branch idbranch, Integer statusvalue, Pageable pagination);

	@Query("SELECT pb.idproducthistory.idproduct FROM ProductBranch pb WHERE pb.saledate BETWEEN ?1 AND ?2 AND pb.idbranch  = ?3 AND pb.statusvalue = ?4 "
			+ "GROUP BY pb.idproducthistory.idproduct ORDER BY SUM(pb.idproducthistory.saleprice) ASC")
	@Async
	CompletableFuture<List<Product>> getProductTopDownFiveBySaledateAndIdbranchAndStatus(String initdate, String enddate, Branch idbranch, Integer statusvalue, Pageable pagination);
	
	List<Product> findAllByQuantitylowerlimitIsNull();
	
}
