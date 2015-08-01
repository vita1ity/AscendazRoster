package com.ascendaz.roster.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "shift_pattern")
public class ShiftPattern {
	
	@Id
	@GeneratedValue
	@Column(name = "SHIFT_PATTERN_ID", nullable = false)
	private int id;
	
	@Column(name = "PATTERN", nullable = false, length = 30)
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "shiftPattern")
	private Set<StaffShift> staffShiftSet = new HashSet<StaffShift>();
	
}
