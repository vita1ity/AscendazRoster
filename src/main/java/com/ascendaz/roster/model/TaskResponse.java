package com.ascendaz.roster.model;

import java.io.Serializable;
import java.util.Set;

import org.joda.time.LocalDate;

import com.ascendaz.roster.model.attributes.Leave;
import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.attributes.Training;

public class TaskResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5513440258410533243L;
	
	private boolean violated;
	private String status;
	
	//private LocalDate date;
	private int year;
	private int month;
	private int day;
	
	private Shift shift;
	private Set<Training> trainings;
	private Leave leave;
	
	/*public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}*/
	/*public TaskResponse(LocalDate date, boolean isViolated, String status, Shift shift, Set<Training> trainings) {
		super();
		this.date = date;
		this.shift = shift;
		this.violated = isViolated;
		this.status = status;
		this.trainings = trainings;
	}
	public TaskResponse(LocalDate date, boolean isViolated, String status, Shift shift, Set<Training> trainings, Leave leave) {
		super();
		this.date = date;
		this.shift = shift;
		this.violated = isViolated;
		this.status = status;
		this.trainings = trainings;
		this.leave = leave;
	}*/
	
	public TaskResponse(int year, int month, int day, boolean isViolated, String status, Shift shift, Set<Training> trainings) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
		
		this.shift = shift;
		this.violated = isViolated;
		this.status = status;
		this.trainings = trainings;
	}
	public TaskResponse(int year, int month, int day, boolean isViolated, String status, Shift shift, Set<Training> trainings, Leave leave) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
		
		this.shift = shift;
		this.violated = isViolated;
		this.status = status;
		this.trainings = trainings;
		this.leave = leave;
	}
	
	public Shift getShift() {
		return shift;
	}
	public void setShift(Shift shift) {
		this.shift = shift;
	}
	public Set<Training> getTrainings() {
		return trainings;
	}
	public void setTrainings(Set<Training> trainings) {
		this.trainings = trainings;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean getViolated() {
		return violated;
	}
	public void setViolated(boolean isViolated) {
		this.violated = isViolated;
	}
	public Leave getLeave() {
		return leave;
	}

	public void setLeave(Leave leave) {
		this.leave = leave;
	}
	
	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
	public int getDay() {
		return day;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
	@Override
	public String toString() {
		return "TaskResponse [violated=" + violated + ", status=" + status + ", year=" + year + ", month=" + month
				+ ", day=" + day + ", shift=" + shift + ", trainings=" + trainings + ", leave=" + leave + "]";
	}
	
}
