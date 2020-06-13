package com.calpullix.db.process.twitter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.twitter.model.Twitter;

@Repository
public interface TwitterRepository extends JpaRepository<Twitter, Integer> {

	Optional<Twitter> findOneByIsactive(Boolean isactive);
	
}
