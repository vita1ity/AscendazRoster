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

import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.attributes.interfaces.Attribute;
import com.ascendaz.roster.model.attributes.interfaces.CustomEquality;
import com.ascendaz.roster.util.Util;

@Entity
@Table(name = "gender")
public class Gender implements Comparable<Gender>, Attribute, CustomEquality, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -598794564037203446L;

	@Id
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
		if (!(obj instanceof Gender)) {
			return false;
		}
		Gender other = (Gender) Util.deproxy(obj);
		if (type == null) {
			if (other.type != null)
				return false;	
		} 
		else if (other.type.equals("A") || this.type.equals(other.type)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return type;
	}
	
	@Override
	public Object getValue() {
		
		return this.type;
	}

	@Override
	public boolean isEqual(Object other) {
		
		return this.equals(other);
	}
	
	
}

