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
@Table(name = "shift")
public class Shift {
	@Id
	@GeneratedValue
	@Column(name="SHIFT_ID", nullable=false)
	private int id;
	
	@Column(name="SHIFT_TYPE", nullable=false, length=30)
	private String shift;
	
	@Column(name="SHIFT_LETTER", nullable=false, length=1)
	private String shiftLetter;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "shift")
	private Set<TaskProfile> taskProfileSet = new HashSet<TaskProfile>();

	public String getShiftLetter() {
		return shiftLetter;
	}

	public void setShiftLetter(String shiftLetter) {
		this.shiftLetter = shiftLetter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}
	
	
}
