package com.ascendaz.roster.model.attributes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.attributes.interfaces.Attribute;

@Entity
@Table(name="skill")
public class Skill implements Comparable<Skill>, Attribute, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2564071317829786884L;

	@Id
	@Column(name="SKILL_ID", nullable=false)
	private int id;
	
	@Column(name="SKILL", nullable=false, length=50)
	private String skill;
	
	@Column(name="SEQUENCE", nullable=false)
	private int sequence;
	
	@ManyToMany(mappedBy = "skillSet")
	private Set<Staff> staffSet = new HashSet<Staff>();
	
	
	@ManyToMany(mappedBy = "skillSet")
	private Set<TaskProfile> taskProfileSet = new HashSet<TaskProfile>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
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

	public Set<TaskProfile> getTaskProfileSet() {
		return taskProfileSet;
	}

	public void setTaskProfileSet(Set<TaskProfile> taskProfileSet) {
		this.taskProfileSet = taskProfileSet;
	}

	@Override
	public int compareTo(Skill skill) {
		
		return this.sequence - skill.getSequence();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((skill == null) ? 0 : skill.hashCode());
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
		Skill other = (Skill) obj;
		if (skill == null) {
			if (other.skill != null)
				return false;
		} else if (!skill.equals(other.skill))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return skill;
	}
	
	@Override
	public Object getValue() {
		
		return this.skill;
	}
	
	
	
}
