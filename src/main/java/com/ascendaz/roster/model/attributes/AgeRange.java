package com.ascendaz.roster.model.attributes;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ascendaz.roster.model.TaskProfile;

@Entity
@Table(name = "age_range")
public class AgeRange implements Range{
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "AGE_RANGE_ID", nullable = false)
	private int id;
	
	@Column(name = "START_AGE", nullable = false)
	private Integer startAge;
	
	@Column(name = "END_AGE", nullable = true)
	private Integer endAge;
	
	@Column(name = "SEQUENCE", nullable = false)
	private int sequence;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "ageRange")
	private Set<TaskProfile> taskProfileSet = new HashSet<TaskProfile>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public Integer getStartAge() {
		return startAge;
	}

	public void setStartAge(Integer startAge) {
		this.startAge = startAge;
	}

	public Integer getEndAge() {
		return endAge;
	}

	public void setEndAge(Integer endAge) {
		this.endAge = endAge;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Set<TaskProfile> getTaskProfileSet() {
		return taskProfileSet;
	}

	public void setTaskProfileSet(Set<TaskProfile> taskProfileSet) {
		this.taskProfileSet = taskProfileSet;
	}

	@Override
	public Number getStartValue() {
		
		return startAge;
	}

	@Override
	public Number getEndValue() {
		
		return endAge;
	}

	@Override
	public boolean checkInBetween(Comparable<Number> object) {
		/*if (number.longValue() > startAge.intValue() && number.longValue() < endAge.intValue()) {
			return true;
		}*/
		if (endAge == null) {
			if (object.compareTo(startAge) > 0) {
				return true;
			}
		}
		else if (object.compareTo(startAge) > 0 && object.compareTo(endAge) < 0) {
			return true;
		}
		
		
		return false;
	}

	@Override
	public String toString() {
		return "[" + startAge + ", " + endAge + "]";
	}
	
	
}
