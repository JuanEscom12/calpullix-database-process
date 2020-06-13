package com.calpullix.db.process.twitter.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.catalog.model.TwitterTypeMessage;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class TwitterMessages {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	@Basic
	@Column(name = "typemessage")
	private Integer typemessagevalue;
	
	@Transient
	private TwitterTypeMessage typemessage;
	
	private String message;
	
	private String atuser;
	
	private String user;
	
	private String date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idtwitter", referencedColumnName = "id")
	private Twitter idtwitter;
	
	@PostLoad
    void fillTransient() {
        if (typemessagevalue > 0) {
            this.typemessage = TwitterTypeMessage.of(typemessagevalue);
        }
    }
 
    @PrePersist
    void fillPersistent() {
        if (BooleanUtils.negate(typemessage == null)) {
            this.typemessagevalue = typemessage.getId();
        }
    }

	
}
