package com.calpullix.db.process.product.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.product.model.ProductBranch;
import com.calpullix.db.process.promotions.model.Promotions;

@Repository
public interface ProductBranchRepository extends JpaRepository<ProductBranch, Integer> {

	@Query("SELECT count(p) FROM ProductBranch p WHERE p.idproducthistory.idproduct = ?1 AND p.idbranch = ?2 AND p.locationvalue = ?3")
	int getCountByIdproductAndIdbranchAndLocation(Product idproduct, Branch idbranch, Integer locationvalue);
	
	@Query("SELECT count(p) FROM ProductBranch p WHERE p.idproducthistory.idproduct = ?1 "
			+ "AND p.idbranch = ?2 AND p.statusvalue = ?3")
	int getNumberProductsByIdbranchAndIdproductAndStatus(Product idproduct, Branch idbranch, Integer statusvalue);
	
	@Query("SELECT count(DISTINCT p.idcustomer) FROM ProductBranch p WHERE p.idpromotion = ?1")
	int getNumberCostumersByIdpromotion(Promotions idpromotion);
	
	@Query("SELECT SUM(p.idproducthistory.saleprice) FROM ProductBranch p WHERE p.idpromotion = ?1")
	BigDecimal getEariningsByIdpromotion(Promotions idpromotion);
	
	@Query("SELECT SUM(pb.idproducthistory.saleprice) FROM ProductBranch pb WHERE pb.idpromotion = ?1")
	BigDecimal getAmountProfitByIdpromotion(Promotions idpromotion);
	
	@Query("SELECT pb FROM ProductBranch pb WHERE pb.idbranch = ?1 AND pb.saledate BETWEEN ?2 AND ?3 AND pb.statusvalue = ?4")
	List<ProductBranch> findAllByIdbranchidAndSaledateAndStatus(Branch idbranch, String initDate, String endDate, Integer statusvalue);
	
	@Query("SELECT pb FROM ProductBranch pb WHERE pb.idbranch = ?1 AND pb.idproducthistory.idproduct = ?2 AND pb.saledate BETWEEN ?3 AND ?4 AND pb.statusvalue = ?5")
	List<ProductBranch> findAllByIdbranchAndIdproductAndSaledateAndStatus(Branch idbranch, Product idproduct, String initDate, String endDate, Integer statusvalue);

	List<ProductBranch> findAllByIdcustomerIsNull();
	
	List<ProductBranch> findAllByIdpromotionIsNotNull(Pageable pagination);
	
	
}
