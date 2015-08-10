package com.ascendaz.roster.model.attributes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ascendaz.roster.model.StaffTraining;
import com.ascendaz.roster.model.TaskProfile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "training")
public class Training implements Attribute, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3459850485284595910L;

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TRAINING_ID", nullable = false)
	private int id;
	
	@Column(name = "NAME", nullable = false, length = 50)
	private String name;
	
	@Column(name = "DESCRIPTION", nullable = true, length = 200)
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "training")
	@JsonIgnore
	private Set<StaffTraining> staffTrainingSet = new HashSet<StaffTraining>();
	
	@ManyToMany(mappedBy = "trainingSet")
	@JsonBackReference
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
		Training other = (Training) obj;
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
