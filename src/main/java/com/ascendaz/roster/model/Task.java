package com.ascendaz.roster.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import com.ascendaz.roster.model.attributes.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "task")
public class Task implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3486804239461364169L;

	@Id
	@Column(name = "TASK_ID", nullable = false)
	private int id;
	
	@Column(name = "NAME", nullable = false, length = 50)
	private String name;
	
	@Column(name = "START_DATE", nullable = false)
	@JsonIgnore
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate startDate;
	
	@Column(name = "END_DATE", nullable = false)
	@JsonIgnore
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate endDate;
	
	@JoinTable(name = "task_day_of_week", 
            joinColumns = { 
                   @JoinColumn(name = "TASK_ID", referencedColumnName = "TASK_ID")
            }, 
            inverseJoinColumns = { 
                   @JoinColumn(name = "DAY_OF_WEEK_ID", referencedColumnName = "DAY_OF_WEEK_ID")
            }
     )
    @ManyToMany
    @JsonIgnore
    private List<DayOfWeek> dayOfWeekSet = new ArrayList<DayOfWeek>();
	
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
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	

	public List<DayOfWeek> getDayOfWeekSet() {
		return dayOfWeekSet;
	}

	public void setDayOfWeekSet(List<DayOfWeek> dayOfWeekSet) {
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
