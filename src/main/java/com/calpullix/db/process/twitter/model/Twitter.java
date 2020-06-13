package com.calpullix.db.process.twitter.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Twitter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	private String date;
	
	private String keywordone;
	
	private String keywordtwo;
	
	private String keywordthree;
	
	private String keywordfour;
	
	private String keywordfive;
	
	private Integer percentagepositive;
	
	private Integer percentagenegative;
	
	private Integer percentageneutral;
	
	private String profilename;
	
	private String profilattname;
	
	private Boolean isactive;
	
	@Lob
	private byte[] profilepicture;
	
	@Lob
	private byte[] clowwords;
	
	@OneToMany(
	        mappedBy = "idtwitter",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<TwitterMessages> twitterMessages;
	
}
