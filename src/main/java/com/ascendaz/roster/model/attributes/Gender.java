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
@Table(name = "gender")
public class Gender implements Comparable<Gender> {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="GENDER_ID", nullable=false) 
	private int id;
	
	@Column(name="TYPE", nullable=false, length=1)
	private String type;
	
	@Column(name="NAME", nullable=false, length=10)
	private String name;
	
	@Column(name="SEQUENCE", nullable=false)
	private int sequence;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "gender")
	private Set<Staff> staffSet = new HashSet<Staff>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "gender")
	private Set<TaskProfile> taskProfileSet = new HashSet<TaskProfile>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Set<Staff> getStaff() {
		return staffSet;
	}

	public void setStaff(Set<Staff> staff) {
		this.staffSet = staff;
	}

/*	@Override
	public Object getValue() {
		
		return type;
	}
*/
	@Override
	public int compareTo(Gender gender) {
		
		return this.sequence - gender.getSequence();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Gender other = (Gender) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return type;
	}
	
	
	
}

