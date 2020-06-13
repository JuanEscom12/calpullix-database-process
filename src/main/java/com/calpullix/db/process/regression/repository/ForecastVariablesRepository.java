package com.calpullix.db.process.regression.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.regression.model.ForecastVariables;

@Repository
public interface ForecastVariablesRepository extends JpaRepository<ForecastVariables, Integer> {

}
