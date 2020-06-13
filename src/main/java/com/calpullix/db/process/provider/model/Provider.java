package com.calpullix.db.process.provider.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.catalog.model.ContactType;
import com.calpullix.db.process.product.model.Product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class Provider {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	private String name;
	
	private String address;
	
	private String latitude;
	
	private String longitude;
	
	private String contact;
	
	private String telephone;
	
	private String rfc;
	
	@Basic
	@Column(name = "contacttype")
	private Integer contacttypevalue;
	
	@Transient
	private ContactType contactType;
	
	@OneToMany(
	        mappedBy = "provider",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<Product> product;
	
	@PostLoad
    void fillTransient() {
        if (contacttypevalue > 0) {
            this.contactType = ContactType.of(contacttypevalue);
        }
        
    }
 
    @PrePersist
    void fillPersistent() {
        if (BooleanUtils.negate(contactType == null)) {
            this.contacttypevalue = contactType.getId();
        }
        
    }
	
	
}
