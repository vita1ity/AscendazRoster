package com.ascendaz.roster.model.attributes;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ascendaz.roster.model.Task;
import com.ascendaz.roster.model.TaskProfile;

@Entity
@Table(name = "location")
public class Location implements Attribute{
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "LOCATION_ID", nullable = false)
	private int id;
	
	@Column(name = "LOCATION", nullable = false, length = 50)
	private String location;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "location")
	private Set<TaskProfile> taskSet = new HashSet<TaskProfile>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<TaskProfile> getTaskSet() {
		return taskSet;
	}

	public void setTaskSet(Set<TaskProfile> taskSet) {
		this.taskSet = taskSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
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
		Location other = (Location) obj;
		if (location == null) {
			if (other.getLocation() != null)
				return false;
		} else if (!location.equals(other.getLocation()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return location;
	}

	@Override
	public Object getValue() {
		
		return this.location;
	}

	
}
