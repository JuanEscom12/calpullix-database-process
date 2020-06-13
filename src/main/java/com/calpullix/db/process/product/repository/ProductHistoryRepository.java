package com.calpullix.db.process.product.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.product.model.ProductHistory;


@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Integer> {
	
	@Query(value = "SELECT p FROM ProductHistory p WHERE YEAR(p.creationdate) = ?1")
	List<ProductHistory> findAllProductByYear(int year);
	
	@Query(value = "SELECT p FROM ProductHistory p WHERE YEAR(p.creationdate) = ?1 AND p.idproduct > ?2")
	List<ProductHistory> findAllProductByYearAndIdproduct(int year, Product idproduct);
	
	@Query(value = "SELECT p.purchaseprice FROM ProductHistory p WHERE p.idproduct = ?1 AND p.statusvalue = ?2")
	BigDecimal findPurchasePriceByIdproduct(Product idproduct, Integer statusvalue);
	
	@Query(value = "SELECT p FROM ProductHistory p WHERE p.idproduct = ?1 AND p.statusvalue = ?2")
	ProductHistory findPurchasePriceByIdproductAndStatus(Product idproduct, Integer statusvalue);
	
	@Query(value = "SELECT p FROM ProductHistory p WHERE p.idproduct in ?1 AND p.statusvalue = ?2")
	List<ProductHistory> findPurchasePriceByIdproductAndStatusIn(List<Product> idproduct, Integer statusvalue);
	
	@Query(value = "SELECT p FROM ProductHistory p WHERE p.idproduct > ?1")
	List<ProductHistory> findAllByIdproductGraterthan(Product idproduct);
	
	List<ProductHistory> findAllByIdproductOrderByCreationdateAsc(Product product);

}
