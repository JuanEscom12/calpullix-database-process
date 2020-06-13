package com.calpullix.db.process.product.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class DistributionCenter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;

	private String name;
	
	private String address;
	
	@OneToMany(
	        mappedBy = "distributioncenter",
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<ProductBranch> productBranch;
	
}
