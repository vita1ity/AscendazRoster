package com.ascendaz.roster.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.ascendaz.roster.model.attributes.Leave;
import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.attributes.Training;

public class ScheduleResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7154853085470915709L;
	private String name;
	private String location;
	private int reference;
	
	private int page;
	
	private List<TaskResponse> tasks;
	
	public ScheduleResponse(String name, String location, int reference, List<TaskResponse> tasks) {
		super();
		this.name = name;
		this.location = location;
		this.reference = reference;
		this.tasks = tasks;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getReference() {
		return reference;
	}
	public void setReference(int reference) {
		this.reference = reference;
	}

	public List<TaskResponse> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskResponse> tasks) {
		this.tasks = tasks;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public static List<ScheduleResponse> createScheduleResponse(List<Schedule> schedule, LocalDate startDate, LocalDate endDate, int page) {
		if (schedule == null || schedule.size() == 0) {
			return null;
		}
		Collections.sort(schedule);
		
		
		List<ScheduleResponse> scheduleResponse = new ArrayList<ScheduleResponse>();
		ScheduleResponse response = null;
		
		List<TaskResponse> taskResponseList = null;
		TaskResponse taskResponse = null;
		
		String staffName = null;
		String location = null;
		String status = null;
		int reference = 0;
		
		TaskProfile taskProfile = null;
		Set<StaffLocation> locationSet = null;
		LocalDate effectDate = null;
		LocalDate expireDate = null;
		LocalDate scheduleDate = null;
		String type = null;
		Set<Training> trainings = null;
		Shift shift = null;
		
		for (int i = 0; i < schedule.size(); i++) {
			
			staffName = schedule.get(i).getStaff().getName();
			
			//System.out.println("STAFF NAME: " + staffName);
			reference = schedule.get(i).getStaff().getId();
			location = null;
			taskProfile = schedule.get(i).getTask();
			
			locationSet = schedule.get(i).getStaff().getStaffLocationSet();
			
			
			for (StaffLocation staffLocation: locationSet) {
				effectDate = staffLocation.getEffectiveDate();
				expireDate = staffLocation.getExpireDate();
				type = staffLocation.getType();
				
				/*System.out.println(effectDate);
				System.out.println(expireDate);
				System.out.println(type);*/
				
				if (type.equalsIgnoreCase("assigned")) {
					if (effectDate.compareTo(startDate) <= 0 && expireDate.compareTo(startDate) > 0) {
						
						location = staffLocation.getLocation().getLocation();
						//System.out.println("Valid:" + location);
						break;
					} 
				}
				
			
			}
			
			
			
			if (location == null && taskProfile != null) {
				location = taskProfile.getLocation().getLocation();
			}
			
			LocalDate currentDate = new LocalDate(startDate);
			taskResponseList = new ArrayList<TaskResponse>();
			
			
			int daysBetween = Days.daysBetween(currentDate, endDate).getDays() + 1;
				
			daysOfWeek: for (int j = 0; j < daysBetween; j++) {
				
				int year = currentDate.getYear();
				int month = currentDate.getMonthOfYear();
				int day = currentDate.getDayOfMonth();
				
				while (i < schedule.size() && schedule.get(i).getStaff().getName().equals(staffName)) {
					
					scheduleDate = new LocalDate(schedule.get(i).getDate());
					status = schedule.get(i).getStatus();
					
					//System.out.println(currentDate + ",   " + scheduleDate);
					
					if (currentDate.compareTo(scheduleDate) < 0) {
						// blank task
						
						taskResponse = new TaskResponse(year, month, day, false, status, null, null);
						taskResponseList.add(taskResponse);
						currentDate = currentDate.plusDays(1);
						
						continue daysOfWeek;
					}
					//there is no task for current date
					else if (currentDate.compareTo(scheduleDate) > 0) {
						i++;
						if (i == schedule.size()) {
							while (j < daysBetween) {
								taskResponse = new TaskResponse(year, month, day, false, status, null, null);
								taskResponseList.add(taskResponse);
								++j;
							}
							
						}
						else {
							continue;
						}
						
					}	
					//dates are equal
					else {
						//System.out.println("EQUAL:" + currentDate + ",   " + scheduleDate);
						//assign attributes for the day
						shift = schedule.get(i).getShift();
						Leave leave = null;
						if (shift != null && shift.getShiftLetter().equals("L")) {
							//add leave
							Set<StaffLeave> staffLeaves = schedule.get(i).getStaff().getStaffLeaveSet();
							for (StaffLeave staffLeave: staffLeaves) {
								if (staffLeave.getDate().equals(currentDate)) {
									leave = staffLeave.getLeave();
									break;
								}
							}
						}
						//System.out.println("SHIFT: " + shift.getShift());
						TaskProfile task = schedule.get(i).getTask();
						if (task != null) {
							trainings = task.getTrainingSet();
						}
						else {
							trainings = null;
						}
						boolean isViolated = schedule.get(i).isViolated();
						taskResponse = new TaskResponse(year, month, day, isViolated, status, shift, trainings, leave);
						
						
						taskResponseList.add(taskResponse);
						currentDate = currentDate.plusDays(1);
						
						continue daysOfWeek;
					}
					
				}
				if (i < schedule.size() && !schedule.get(i).getStaff().getName().equals(staffName)) {
					taskResponse = new TaskResponse(year, month, day, false, status, null, null);
					taskResponseList.add(taskResponse);
					--i;
				}
				else if (i >= schedule.size()) {
					break daysOfWeek;
				}
				
			} 
			response = new ScheduleResponse(staffName, location, reference, taskResponseList);
			response.setPage(page);
			scheduleResponse.add(response);
		}
		
		return scheduleResponse;
	}

	@Override
	public String toString() {
		String res = "ScheduleResponse [name=" + name + ", location=" + location + ", reference=" + reference + "\n";
		for (TaskResponse task: tasks) {
			res += task + "\n"; 
		}
		return res;
		
	}
	
}
