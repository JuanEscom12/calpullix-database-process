package com.calpullix.db.process.customer.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.customer.model.CustomerProfile;
import com.calpullix.db.process.customer.model.Customers;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer> {
	
	@Query("SELECT count(c) FROM Customers c WHERE c.idprofile = ?1")
	int getNumberCustomersByIdprofile(CustomerProfile idprofile);
	
	@Query("SELECT count(c) FROM Customers c WHERE c.idprofile = ?1 AND c.classification = ?2 AND id < ?3")
	int getNumberCustomersByIdprofileAndClassificationAndIdLessThan(CustomerProfile idprofile, Integer classification, Integer id);

	@Query("SELECT count(c) FROM Customers c WHERE c.idprofile = ?1 AND c.classification = ?2 AND c.id >= ?3")
	int getNumberCustomersByIdprofileAndClassificationAndIdGraterThan(CustomerProfile idprofile, Integer classification, Integer id);

	
	List<Customers> findAllCustomersByIdprofileIsNull();
	
	List<Customers> findAllCustomersByIdprofileIsNotNull();
	
	List<Customers> findAllCustomersByClassificationIsNull();

	List<Customers> findAllCustomersByClassificationIsNotNull();
	
	List<Customers> findAllByIdprofile(CustomerProfile idprofile);
	
	@Query("SELECT c FROM Customers c WHERE c.idprofile = ?1 AND id < ?2")
	List<Customers> findAllByIdprofileAndIdLessThan(CustomerProfile idprofile, Integer id, Pageable pagination);

	@Query("SELECT c FROM Customers c WHERE c.idprofile = ?1 AND id >= ?2")
	List<Customers> findAllByIdprofileAndIdGreaterThan(CustomerProfile idprofile, Integer id, Pageable pagination);
	
	@Query("SELECT count(c) FROM Customers c WHERE c.idprofile = ?1 AND c.id >= ?2")
	int getNumberCustomersByIdprofileAndIdGreaterThan(CustomerProfile idprofile, Integer id);
	
	@Query("SELECT count(c) FROM Customers c WHERE c.idprofile = ?1 AND c.id < ?2")
	int getNumberCustomersByIdprofileAndIdLessThan(CustomerProfile idprofile, Integer id);
	
	@Query("SELECT c FROM Customers c WHERE c.age BETWEEN ?1 AND ?2 AND c.sex = ?3 AND educationlevelvalue IN(?4) AND statevalue IN (?5) AND maritalstatusvalue IN (?6) AND job IN(?7)")
	List<Customers> findAllByAgeAndSexAndEducationlevelAndStateAndMaritalstatusAndJob(
			Integer age, Integer endage, String sex, List<Integer> educationlevelvalue, List<Integer> statevalue, List<Integer> maritalstatusvalue, List<String> job);
	
}
