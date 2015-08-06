package com.ascendaz.roster.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ascendaz.roster.model.attributes.Shift;

@Entity
@Table(name = "schedule")
public class Schedule implements Comparable<Schedule>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, unique = true)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TASK_ID", nullable = true)
	private TaskProfile task;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAFF_ID", nullable = false)
	private Staff staff;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHIFT_ID", nullable = false)
	private Shift shift;
	
	@Column(name = "DATE", nullable = false)
	private Date date;
	
	@Column(name = "IS_VIOLATED", nullable = true)
	private boolean isViolated;
	
	@Column(name = "STATUS", nullable = false)
	private String status;
	
	public Schedule() {
		super();
	}
	
	public Schedule(TaskProfile task, Staff staff, Date date, Shift shift, boolean isViolated) {
		super();
		this.task = task;
		this.staff = staff;
		this.date = date;
		this.shift = shift;
		this.isViolated = isViolated;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
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
	
	@Override
	public String toString() {
		String str = "Schedule item:\n";
		str += task + "\n";
		str += staff + "\n";
		str += "Date: " + date + "\n";
		str += "Is violated: " + isViolated + "\n";
		return str;
	}


	@Override
	public int compareTo(Schedule o) {

		return this.getDate().compareTo(o.getDate());
	}
}
