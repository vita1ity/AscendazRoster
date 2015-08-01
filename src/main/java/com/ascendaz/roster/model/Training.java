package com.ascendaz.roster.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "training")
public class Training {
	
	@Id
	@GeneratedValue
	@Column(name = "TRAINING_ID", nullable = false)
	private int id;
	
	@Column(name = "NAME", nullable = false, length = 50)
	private String name;
	
	@Column(name = "DESCRIPTION", nullable = true, length = 200)
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "training")
	private Set<StaffTraining> staffTrainingSet = new HashSet<StaffTraining>();
	
	@ManyToMany(mappedBy = "trainingSet")
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<StaffTraining> getStaffTrainingSet() {
		return staffTrainingSet;
	}

	public void setStaffTrainingSet(Set<StaffTraining> staffTrainingSet) {
		this.staffTrainingSet = staffTrainingSet;
	}

	public Set<TaskProfile> getTaskProfileSet() {
		return taskProfileSet;
	}

	public void setTaskProfileSet(Set<TaskProfile> taskProfileSet) {
		this.taskProfileSet = taskProfileSet;
	}
	
}
