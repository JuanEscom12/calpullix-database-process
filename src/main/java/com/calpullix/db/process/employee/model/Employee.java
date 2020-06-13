package com.calpullix.db.process.employee.model;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.GenericGenerator;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.catalog.model.EmployeePosition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	
	private String name;
	
	@Basic
	@Column(name = "position")
	private Integer positionvalue;
	
	@Transient
	private EmployeePosition position;
	
	private String sex;
	
	private Integer age;
	
	private String address;
	
	private BigDecimal monthlysalary;
	
	private BigDecimal isr;
	
	@OneToOne(mappedBy = "manager", fetch = FetchType.LAZY)
	private Branch branch;
	
	
	@PostLoad
    void fillTransient() {
        if (positionvalue > 0) {
            this.position = EmployeePosition.of(positionvalue);
        }
    }
 
    @PrePersist
    void fillPersistent() {
        if (BooleanUtils.negate(position == null)) {
            this.positionvalue = position.getIdPosition();
        }
    }
    
}
