package com.ascendaz.roster.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import com.ascendaz.roster.model.attributes.Shift;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "schedule")
public class Schedule implements Comparable<Schedule>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1531670117193524125L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, unique = true)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TASK_ID", nullable = true)
	@JsonManagedReference
	private TaskProfile task;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAFF_ID", nullable = false)
	@JsonManagedReference
	private Staff staff;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHIFT_ID", nullable = false)
	@JsonManagedReference
	private Shift shift;
	
	@Column(name = "DATE", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate date;
	
	@Column(name = "IS_VIOLATED", nullable = true)
	private boolean isViolated;
	
	@Column(name = "STATUS", nullable = false)
	private String status;
	
	public Schedule() {
		super();
	}
	
	public Schedule(TaskProfile task, Staff staff, LocalDate date, Shift shift, String status, boolean isViolated) {
		super();
		this.task = task;
		this.staff = staff;
		this.date = date;
		this.shift = shift;
		this.status = status;
		this.isViolated = isViolated;
	}
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TaskProfile getTask() {
		return task;
	}

	public void setTask(TaskProfile task) {
		this.task = task;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public boolean isViolated() {
		return isViolated;
	}

	public void setViolated(boolean isViolated) {
		this.isViolated = isViolated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public Shift getShift() {
		return shift;
	}

	public void setShift(Shift shift) {
		this.shift = shift;
	}

	@Override
	public String toString() {
		String str = "Schedule item:\n";
		str += task + "\n";
		str += staff + "\n";
		str += "Shift: " + shift + "\n";
		str += "Date: " + date + "\n";
		str += "Is violated: " + isViolated + "\n";
		return str;
	}


	@Override
	public int compareTo(Schedule o) {
		int staffComparison = this.staff.compareTo(o.staff);
		if (staffComparison == 0) {
			return this.date.compareTo(o.date);
		}
		else {
			return staffComparison;
		}
	}
}
