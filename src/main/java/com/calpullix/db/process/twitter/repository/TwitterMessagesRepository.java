package com.calpullix.db.process.twitter.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.calpullix.db.process.twitter.model.Twitter;
import com.calpullix.db.process.twitter.model.TwitterMessages;

@Repository
public interface TwitterMessagesRepository extends JpaRepository<TwitterMessages, Integer> {

	@Query("SELECT t FROM TwitterMessages t WHERE t.idtwitter = ?1 AND t.typemessagevalue = ?2")
	List<TwitterMessages> findAllByIdtwitterAndTypemessagevalue(Twitter idtwitter, Integer typemessagevalue, Pageable page);
	
	@Query("SELECT COUNT(t) FROM TwitterMessages t WHERE t.idtwitter = ?1 ")
	int getCountTwitterMessagesByIdtwitter(Twitter idtwitter);

	@Query("SELECT COUNT(t) FROM TwitterMessages t WHERE t.idtwitter = ?1 AND t.typemessagevalue = ?2 ")
	int getCountTwitterMessagesByIdtwitterAndTypemessagevalue(Twitter idtwitter, Integer typemessagevalue);

}
