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

import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.TaskProfile;

@Entity
@Table(name = "designation")
public class Designation implements Comparable<Designation>, Attribute {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DESIGNATION_ID", nullable = false)
	private int id;
	
	@Column(name = "NAME", nullable = false)
	private String name;
	
	@Column(name = "SEQUENCE", nullable = false)
	private int sequence;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "designation")
	private Set<Staff> staffSet = new HashSet<Staff>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "designation")
	private Set<TaskProfile> taskProfileSet = new HashSet<TaskProfile>();

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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Set<Staff> getStaffSet() {
		return staffSet;
	}

	public void setStaffSet(Set<Staff> staffSet) {
		this.staffSet = staffSet;
	}

	@Override
	public int compareTo(Designation des) {
		
		return this.sequence - des.getSequence();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Designation other = (Designation) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Object getValue() {
		
		return this.name;
	}
	
	
}
