package com.ascendaz.roster.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "task_profile")
public class TaskProfile {

	@Id
	@GeneratedValue
	@Column(name = "TASK_PROFILE_ID", nullable = false)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TASK_ID", nullable = false)
	private Task task;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DESIGNATION_ID", nullable = false)
	private Designation designation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGE_RANGE_ID", nullable = false)
	private AgeRange ageRange;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GENDER_ID", nullable = false)
	private Gender gender;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHIFT_ID", nullable = false)
	private Shift shift;
	
	@JoinTable(name = "task_profile_skill", 
            joinColumns = { 
                   @JoinColumn(name = "TASK_PROFILE_ID", referencedColumnName = "TASK_PROFILE_ID")
            }, 
            inverseJoinColumns = { 
                   @JoinColumn(name = "SKILL_ID", referencedColumnName = "SKILL_ID")
            }
     )
    @ManyToMany
    private Set<Skill> skillSet = new HashSet<Skill>();
	
	@JoinTable(name = "task_profile_training", 
            joinColumns = { 
                   @JoinColumn(name = "TASK_PROFILE_ID", referencedColumnName = "TASK_PROFILE_ID")
            }, 
            inverseJoinColumns = { 
                   @JoinColumn(name = "TRAINING_ID", referencedColumnName = "TRAINING_ID")
            }
     )
    @ManyToMany
    private Set<Training> trainingSet = new HashSet<Training>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public AgeRange getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(AgeRange ageRange) {
		this.ageRange = ageRange;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Shift getShift() {
		return shift;
	}

	public void setShift(Shift shift) {
		this.shift = shift;
	}

	public Set<Skill> getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(Set<Skill> skillSet) {
		this.skillSet = skillSet;
	}

	public Set<Training> getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(Set<Training> trainingSet) {
		this.trainingSet = trainingSet;
	}
	
	
}
