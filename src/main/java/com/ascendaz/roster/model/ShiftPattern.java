package com.ascendaz.roster.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "shift_pattern")
public class ShiftPattern implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4312301109599608267L;

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SHIFT_PATTERN_ID", nullable = false)
	private int id;
	
	@Column(name = "PATTERN", nullable = false, length = 30)
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "shiftPattern")
	private Set<StaffShift> staffShiftSet = new HashSet<StaffShift>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<StaffShift> getStaffShiftSet() {
		return staffShiftSet;
	}

	public void setStaffShiftSet(Set<StaffShift> staffShiftSet) {
		this.staffShiftSet = staffShiftSet;
	}
	
	
	
}
