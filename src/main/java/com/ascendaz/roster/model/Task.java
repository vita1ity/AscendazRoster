package com.ascendaz.roster.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "task")
public class Task {
	
	@Id
	@GeneratedValue
	@Column(name = "TASK_ID", nullable = false)
	private int id;
	
	@Column(name = "NAME", nullable = false, length = 50)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCATION_ID", nullable = false)
	private Location location;
	
	@Column(name = "START_DATE", nullable = false)
	private Date startDate;
	
	@Column(name = "END_DATE", nullable = false)
	private Date endDate;
	
	@JoinTable(name = "task_day_of_week", 
            joinColumns = { 
                   @JoinColumn(name = "TASK_ID", referencedColumnName = "TASK_ID")
            }, 
            inverseJoinColumns = { 
                   @JoinColumn(name = "DAY_OF_WEEK_ID", referencedColumnName = "DAY_OF_WEEK_ID")
            }
     )
    @ManyToMany
    private Set<DayOfWeek> dayOfWeekSet = new HashSet<DayOfWeek>();
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DAY_OF_WEEK_ID", nullable = false)
	private DayOfWeek dayOfWeek;*/

	@Column(name = "HEADCOUNT", nullable = false)
	private int headcount;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "task")
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	

	public Set<DayOfWeek> getDayOfWeekSet() {
		return dayOfWeekSet;
	}

	public void setDayOfWeekSet(Set<DayOfWeek> dayOfWeekSet) {
		this.dayOfWeekSet = dayOfWeekSet;
	}

	public Set<TaskProfile> getTaskProfileSet() {
		return taskProfileSet;
	}

	public void setTaskProfileSet(Set<TaskProfile> taskProfileSet) {
		this.taskProfileSet = taskProfileSet;
	}

	public int getHeadcount() {
		return headcount;
	}

	public void setHeadcount(int headcount) {
		this.headcount = headcount;
	}
	
	
	
}
