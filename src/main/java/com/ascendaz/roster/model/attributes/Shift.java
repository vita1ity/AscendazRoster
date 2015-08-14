package com.ascendaz.roster.model.attributes;

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

import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.model.TaskProfile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "shift")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shift implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8759897001095444153L;

	@Id
	@Column(name="SHIFT_ID", nullable=false)
	private int id;
	
	@Column(name="SHIFT_TYPE", nullable=false, length=30)
	private String shift;
	
	

	@Column(name="SHIFT_LETTER", nullable=false, length=2)
	private String shiftLetter;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "shift")
	@JsonIgnore
	private Set<TaskProfile> taskProfileSet = new HashSet<TaskProfile>();

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "shift")
	@JsonBackReference
	private Set<Schedule> schedule = new HashSet<Schedule>();
	
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
	
	@Override
	public String toString() {
		return "Shift: " + shiftLetter + " - " + shift;
	}
	
}
