package com.ascendaz.roster.model;

import java.util.List;

public class FilterRequestJson {
	
	private boolean leaveTasks;
	private boolean locationTasks;
	private boolean withoutTasks;
	private boolean rulesViolated;
	private List<String> locations;
	
	public boolean getLeaveTasks() {
		return leaveTasks;
	}
	public void setLeaveTasks(boolean leaveTasks) {
		this.leaveTasks = leaveTasks;
	}
	public boolean getLocationTasks() {
		return locationTasks;
	}
	public void setLocationTasks(boolean locationTasks) {
		this.locationTasks = locationTasks;
	}
	public boolean getWithoutTasks() {
		return withoutTasks;
	}
	public void setWithoutTasks(boolean withoutTasks) {
		this.withoutTasks = withoutTasks;
	}
	public boolean getRulesViolated() {
		return rulesViolated;
	}
	public void setRulesViolated(boolean rulesViolated) {
		this.rulesViolated = rulesViolated;
	}
	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	
	
}
