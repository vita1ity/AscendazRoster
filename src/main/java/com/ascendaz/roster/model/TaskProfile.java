package com.ascendaz.roster.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ascendaz.roster.model.attributes.AgeRange;
import com.ascendaz.roster.model.attributes.Designation;
import com.ascendaz.roster.model.attributes.Gender;
import com.ascendaz.roster.model.attributes.Location;
import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.attributes.Skill;
import com.ascendaz.roster.model.attributes.Training;

@Entity
@Table(name = "task_profile")

/*
@NamedQueries({
	@NamedQuery(name = TaskProfile.GET_ALL_TASKS, query = "SELECT task1 "
												+ "FROM TaskProfile task1")
})*/
public class TaskProfile {

	//public static final String GET_ALL_TASKS = "getTasks";
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TASK_PROFILE_ID", nullable = false)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TASK_ID", nullable = false)
	private Task task;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCATION_ID", nullable = false)
	private Location location;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DESIGNATION_ID", nullable = false)
	private Designation designation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGE_RANGE_ID", nullable = true)
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
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "task")
	private Set<Schedule> scheduleSet = new HashSet<Schedule>();
	
	@Transient
	private boolean needsToBePerformed;

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
	
	

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Set<Schedule> getScheduleSet() {
		return scheduleSet;
	}

	public void setScheduleSet(Set<Schedule> scheduleSet) {
		this.scheduleSet = scheduleSet;
	}

	public boolean isNeedsToBePerformed() {
		return needsToBePerformed;
	}

	public void setNeedsToBePerformed(boolean needsToBePerformed) {
		this.needsToBePerformed = needsToBePerformed;
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

	@Override
	public String toString() {
		return "TaskProfile: id=" + id + ", location=" + location + ", designation=" + designation
				+ ", ageRange=" + ageRange + ", gender=" + gender + ", shift=" + shift;
	}
	
	
}
