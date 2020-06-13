package com.calpullix.db.process.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.calpullix.db.process.statistics.model.StatisticsCorrelationVariableRelation;

public interface StatisticsCorrelationVariableRelationRepository extends JpaRepository<StatisticsCorrelationVariableRelation, Integer> {

}
