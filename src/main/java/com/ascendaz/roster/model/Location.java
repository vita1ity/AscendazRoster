package com.ascendaz.roster.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "location")
public class Location {
	
	@Id
	@GeneratedValue
	@Column(name = "LOCATION_ID", nullable = false)
	private int id;
	
	@Column(name = "LOCATION", nullable = false, length = 50)
	private String location;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "location")
	private Set<Task> taskSet = new HashSet<Task>();
	
}
