package com.ascendaz.roster.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="skill")
public class Skill {
	
	@Id
	@GeneratedValue
	@Column(name="SKILL_ID", nullable=false)
	private int id;
	
	@Column(name="SKILL", nullable=false, length=50)
	private String skill;
	
	@Column(name="SEQUENCE", nullable=false)
	private int sequence;
	
	@ManyToMany(mappedBy = "skillSet")
	private Set<Staff> staffSet = new HashSet<Staff>();
	
	/*@ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "skill")
	private Set<StaffSkill> staffSkillSet = new HashSet<StaffSkill>();*/
	
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
	
	
	
}
