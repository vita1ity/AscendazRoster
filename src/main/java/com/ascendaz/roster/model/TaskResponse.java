package com.ascendaz.roster.model;

import java.io.Serializable;
import java.util.Set;

import org.joda.time.LocalDate;

import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.attributes.Training;

public class TaskResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5513440258410533243L;
	
	private boolean violated;
	private String status;
	private LocalDate date;
	private Shift shift;
	private Set<Training> trainings;
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public TaskResponse(LocalDate date, boolean isViolated, String status, Shift shift, Set<Training> trainings) {
		super();
		this.date = date;
		this.shift = shift;
		this.violated = isViolated;
		this.status = status;
		this.trainings = trainings;
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
	
	@Override
	public String toString() {
		return "TaskResponse [date=" + date + ", shift=" + shift + ", trainings=" + trainings + "]";
	}
	
	
	
}
