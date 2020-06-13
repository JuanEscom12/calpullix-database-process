package com.calpullix.db.process.employee.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Embeddable
public class BranchEmployeeId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer idbranch;
	
	private Integer idemployee;
	
	public BranchEmployeeId(Integer idbranch, Integer idemployee) {
        this.idbranch = idbranch;
        this.idemployee = idemployee;
    }
	
}
