package com.calpullix.db.process.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.statistics.model.StatisticsVariableRelation;

@Repository
public interface StatisticsVariableRelationRepository extends JpaRepository<StatisticsVariableRelation, Integer> {
	

}
