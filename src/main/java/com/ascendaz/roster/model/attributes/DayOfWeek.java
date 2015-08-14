package com.ascendaz.roster.model.attributes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.ascendaz.roster.model.Task;

@Entity
@Table(name = "day_of_week")
public class DayOfWeek implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3637587172783465333L;
	@Id
	@Column(name = "DAY_OF_WEEK_ID", nullable = false)
	private int id;

	@Column(name = "DAY_OF_WEEK", nullable = false, length = 20)
	private String dayOfWeek;
	
	@Column(name = "DAY_NUMBER", nullable = true)
	private int number;
	
	@ManyToMany(mappedBy = "dayOfWeekSet")
	private Set<Task> taskSet = new HashSet<Task>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Set<Task> getTaskSet() {
		return taskSet;
	}

	public void setTaskSet(Set<Task> taskSet) {
		this.taskSet = taskSet;
	}

	@Override
	public String toString() {
		return "DayOfWeek: " + number + ". " + dayOfWeek;
	}
	
	
	
}
