package com.calpullix.db.process.purchaseorder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.purchaseorder.model.Purchaseorder;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<Purchaseorder, Integer> {

	@Query("SELECT count(p) FROM Purchaseorder p")
	int getPurchaseorderCount();

	@Query("SELECT count(p) FROM Purchaseorder p WHERE p.idbranch = ?1")
	int getPurchaseorderByIdbranchCount(Branch idbranch);

	@Query("SELECT count(p) FROM Purchaseorder p WHERE p.idbranch = ?1 AND p.creationdate BETWEEN ?2 AND ?3 AND p.statusvalue = ?4")
	int getPurchaseorderByIdbranchAndCreationDateCount(Branch idbranch, String initDate, String endDate,
			Integer statusvalue);

	@Query("SELECT count(p) FROM Purchaseorder p WHERE p.idbranch = ?1 AND p.deliverydate BETWEEN ?2 AND ?3 AND p.statusvalue = ?4")
	int getPurchaseorderByIdbranchAndDeliverydateCount(Branch idbranch, String initDate, String endDate,
			Integer statusvalue);

	@Query("SELECT count(p) FROM Purchaseorder p WHERE p.creationdate BETWEEN ?1 AND ?2 AND p.statusvalue = ?3")
	int getPurchaseorderByCreationDateAndStatusCount(String initDate, String endDate, Integer statusvalue);

	@Query("SELECT count(p) FROM Purchaseorder p WHERE p.deliverydate BETWEEN ?1 AND ?2 AND p.statusvalue = ?3")
	int getPurchaseorderByDeliverydateAndStatusCount(String initDate, String endDate, Integer statusvalue);

	@Query("SELECT count(p) FROM Purchaseorder p WHERE p.idbranch = ?1 AND p.statusvalue = ?2")
	int getPurchaseorderByIdbranchAndStatusCount(Branch idbranch, Integer statusvalue);

	@Query("SELECT count(p) FROM Purchaseorder p WHERE p.statusvalue = ?1")
	int getPurchaseorderByStatusCount(Integer statusvalue);

	@Query("SELECT count(p) FROM Purchaseorder p WHERE p.creationdate BETWEEN ?1 AND ?2")
	int getPurchaseorderByCreationdateCount(String initDate, String endDate);

	@Query("SELECT count(p) FROM Purchaseorder p WHERE p.idbranch = ?1 AND p.creationdate BETWEEN ?2 AND ?3")
	int getPurchaseorderByIdbranchAndCreationdateCount(Branch idbranch, String initDate, String endDate);

	
	Page<Purchaseorder> findAllByIdbranch(Branch idbranch, Pageable pagination);

	@Query("SELECT p FROM Purchaseorder p WHERE p.idbranch = ?1 AND p.creationdate BETWEEN ?2 AND ?3 AND p.statusvalue = ?4")
	Page<Purchaseorder> findAllByIdbranchAndCreationdateAndStatus(Branch idbranch, String initDate, String endDate,
			Integer statusvalue, Pageable pagination);

	@Query("SELECT p FROM Purchaseorder p WHERE p.idbranch = ?1 AND p.deliverydate BETWEEN ?2 AND ?3 AND p.statusvalue = ?4")
	Page<Purchaseorder> findAllByIdbranchAndDeliverydateAndStatus(Branch idbranch, String initDate, String endDate,
			Integer statusvalue, Pageable pagination);

	@Query("SELECT p FROM Purchaseorder p WHERE p.creationdate BETWEEN ?1 AND ?2 AND p.statusvalue = ?3")
	Page<Purchaseorder> findAllCreationDateAndStatus(String initDate, String endDate,
			Integer statusvalue, Pageable pagination);

	@Query("SELECT p FROM Purchaseorder p WHERE p.deliverydate BETWEEN ?1 AND ?2 AND p.statusvalue = ?3")
	Page<Purchaseorder> findAllByDeliverydateAndStatus(String initDate, String endDate, Integer statusvalue,
			Pageable pagination);

	@Query("SELECT p FROM Purchaseorder p WHERE p.statusvalue = ?1")
	Page<Purchaseorder> findAllByStatus(Integer statusvalue, Pageable pagination);
	
	@Query("SELECT p FROM Purchaseorder p WHERE p.creationdate BETWEEN ?1 AND ?2")
	Page<Purchaseorder> findAllByCreationdate(String initDate, String endDate, Pageable pagination);
	
	@Query("SELECT p FROM Purchaseorder p WHERE p.idbranch = ?1 AND p.statusvalue = ?2")
	Page<Purchaseorder> findAllByIdbranchAndStatus(Branch idbranch, Integer statusvalue, Pageable pagination);
	
	@Query("SELECT p FROM Purchaseorder p WHERE p.idbranch = ?1 AND p.creationdate BETWEEN ?2 AND ?3")
	Page<Purchaseorder> findAllByIdbranchAndCreationdate(Branch idbranch, String initDate, String endDate, Pageable pagination);

}
