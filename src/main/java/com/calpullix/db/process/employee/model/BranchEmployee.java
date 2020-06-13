package com.calpullix.db.process.employee.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class BranchEmployee {
	
	@EmbeddedId
	private BranchEmployeeId branchEmployeeId;
	
	private Boolean active;
	
}
