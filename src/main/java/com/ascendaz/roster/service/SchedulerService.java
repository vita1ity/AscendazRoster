package com.ascendaz.roster.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascendaz.roster.engine.RosterEngineMulti;
import com.ascendaz.roster.exception.RosterEngineException;
import com.ascendaz.roster.model.FilterRequestJson;
import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.model.ScheduleResponse;
import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.TaskResponse;
import com.ascendaz.roster.model.attributes.Location;
import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.config.Rule;
import com.ascendaz.roster.repository.SchedulerRepository;
import com.ascendaz.roster.util.Constants;

@Service("scheduleService")
public class SchedulerService {
	
	@Autowired
	private SchedulerRepository schedulerRepository;
	
	static Logger logger = LoggerFactory.getLogger(SchedulerService.class);
	
	public List<ScheduleResponse> processRules(LocalDate startDate, LocalDate endDate, boolean considerSalary) 
			throws RosterEngineException, InterruptedException {
		
		MDC.put("logFileName", "process-rules");
		
		long start = System.currentTimeMillis();
		
		schedulerRepository.removeSchedule(startDate, endDate);
		
		List<TaskProfile> tasksProfile = schedulerRepository.getTasks();
		List<Staff> staff = schedulerRepository.getActiveStaff(startDate, endDate);
		List<Rule> rules = schedulerRepository.getSelectedRules();
		Shift leaveShift = schedulerRepository.getShiftByShiftLetter("L");
		Shift dayOffShift = schedulerRepository.getShiftByShiftLetter("O");
		
		
		RosterEngineMulti engine = new RosterEngineMulti(tasksProfile, staff, rules, leaveShift, dayOffShift);
		//RosterEngine engine = new RosterEngine(tasksProfile, staff, rules, leaveShift, dayOffShift);
		
		
		List<Schedule> schedule = null;
		
		schedule = engine.processRules(startDate, endDate, considerSalary);
		//System.out.println(engine.getLog());
		
		schedulerRepository.saveSchedule(schedule);
		
		//get all staff if schedule wasn't generated yet
		if (schedule == null || schedule.size() == 0) {
			
			schedule = new ArrayList<Schedule>();
			for (Staff s: staff) {
				LocalDate currentDate = startDate;
				while (currentDate.compareTo(endDate) <= 0) {
					schedule.add(new Schedule(null, s, currentDate, null, null, false));
					
					currentDate = currentDate.plusDays(1);
				}
					
			}
		}
		
		List<Staff> pageStaff = schedulerRepository.getActiveStaffForPage(startDate, endDate, 1);
		List<Schedule> pageSchedule = new ArrayList<Schedule>();
		for (Schedule s: schedule) {
			if (pageStaff.contains(s.getStaff())) {
				pageSchedule.add(s);
			}
		}
		
		List<ScheduleResponse> response = ScheduleResponse.createScheduleResponse(pageSchedule, startDate, endDate, 1);
		
		long end = System.currentTimeMillis();
		logger.debug("PROCESS RULES FINISHED. Time: " + (end - start) / 1000 + "s");
		MDC.remove("logFileName");
		
		/*System.out.println();
		System.out.println();
		System.out.println("SCHEDULER:");
		int i = 0;
		
		for (Schedule s: schedule) {
			System.out.println(i + ". " + s);
			++i;
		}*/
		return response;
	}


	public List<ScheduleResponse> getScheduleForPeriod(LocalDate startDate, LocalDate endDate, int page) {
	
		//List<Staff> staffPage = schedulerRepository.getStaffForPage(page);
		List<Integer> staffPage = schedulerRepository.getStaffForPage(startDate, endDate, page);
		
		List<Schedule> schedule = schedulerRepository.getScheduleForPeriod(startDate, endDate, staffPage);
		
		//get all staff if schedule wasn't generated yet
		if (schedule == null || schedule.size() == 0) {
			
			List<Staff> staff = schedulerRepository.getActiveStaffForPage(startDate, endDate, page);
			schedule = new ArrayList<Schedule>();
			for (Staff s: staff) {
				LocalDate currentDate = startDate;
				while (currentDate.compareTo(endDate) <= 0) {
					schedule.add(new Schedule(null, s, currentDate, null, null, false));
					
					currentDate = currentDate.plusDays(1);
				}
					
			}
		}
		
		/*for (Schedule sch: schedule) {
			System.out.println(sch);
		}*/
		
		List<ScheduleResponse> scheduleResponse = ScheduleResponse.createScheduleResponse(schedule, startDate, endDate, page);
		
		/*for (ScheduleResponse sch: scheduleResponse) {
			System.out.println(sch);
		}*/
	
		return scheduleResponse;
		
	}


	public List<ScheduleResponse> setApprovedStatus(List<ScheduleResponse> schedule) {
		for (ScheduleResponse scheduleResp: schedule) {
			for (TaskResponse task: scheduleResp.getTasks()) {
				task.setStatus("Submitted");
			}
			
		}
		List<TaskResponse> tasks = schedule.get(0).getTasks();
		
		/*LocalDate startDate = tasks.get(0).getDate();
		LocalDate endDate = tasks.get(tasks.size() - 1).getDate();*/
		
		
		int year = tasks.get(0).getYear();
		int month = tasks.get(0).getMonth();
		int day = tasks.get(0).getDay();
		LocalDate startDate = new LocalDate(year, month, day);
		
		year = tasks.get(tasks.size() - 1).getYear();
		month = tasks.get(tasks.size() - 1).getMonth();
		day = tasks.get(tasks.size() - 1).getDay();
		LocalDate endDate = new LocalDate(year, month, day);
		
		schedulerRepository.setApprovedStatus(startDate, endDate);
		return schedule;
	}


	public List<ScheduleResponse> getStaffWithoutTasks(List<ScheduleResponse> schedule) {
		List<ScheduleResponse> withoutTaskSchedule = new ArrayList<ScheduleResponse>();
		if (schedule == null) {
			return null;
		}
		for (ScheduleResponse sr: schedule) {
			boolean withoutTask = false;
			for (TaskResponse tr: sr.getTasks()) {
				if (tr.getShift() == null) {
					withoutTask = true;
					break;
				}
			}
			if (withoutTask) {
				withoutTaskSchedule.add(sr);
			}
		}
		return withoutTaskSchedule;
	}


	public List<Location> getAllLocations() {
		
		return schedulerRepository.getAllLocations();
	}


	public List<ScheduleResponse> getScheduleForLocations(List<ScheduleResponse> schedule,
			List<String> locations) {
		List<ScheduleResponse> filteredLocationsSchedule = new ArrayList<ScheduleResponse>();
		if (schedule == null) {
			return null;
		}
		for (ScheduleResponse sr: schedule) {
			
			String location = sr.getLocation();
			for (String l: locations) {
				if (location != null && location.equals(l)) {
					
					filteredLocationsSchedule.add(sr);
					break;
				}
			}
		}
		
		return filteredLocationsSchedule;
	}


	public List<ScheduleResponse> getRulesViolatedTasks(List<ScheduleResponse> schedule) {
		List<ScheduleResponse> rulesViolatedSchedule = new ArrayList<ScheduleResponse>();
		if (schedule == null) {
			return null;
		}
		for (ScheduleResponse sr: schedule) {
			for (TaskResponse tr: sr.getTasks()) {
				if (tr.getViolated()) {
					rulesViolatedSchedule.add(sr);
					break;
				}
			}
		}
		
		return rulesViolatedSchedule;
	}


	public List<ScheduleResponse> getLeavesTasks(List<ScheduleResponse> schedule) {
		List<ScheduleResponse> leavesSchedule = new ArrayList<ScheduleResponse>();
		if (schedule == null) {
			return null;
		}
		Shift shift = null;
		for (ScheduleResponse sr: schedule) {
			for (TaskResponse tr: sr.getTasks()) {
				shift = tr.getShift();
				if (shift != null && shift.getShiftLetter().equals("L")) {
					leavesSchedule.add(sr);
					break;
				}
				
			}
		}
		return leavesSchedule;
	}


	public List<ScheduleResponse> applyFilters(FilterRequestJson filterRequest,
			List<ScheduleResponse> sessionSchedule) {
		if (filterRequest.getLeaveTasks()) {
			sessionSchedule = getLeavesTasks(sessionSchedule);
		}
		if (filterRequest.getLocationTasks()) {
			sessionSchedule = getScheduleForLocations(sessionSchedule, filterRequest.getLocations());
		}
		if (filterRequest.getRulesViolated()) {
			sessionSchedule = getRulesViolatedTasks(sessionSchedule);
		}
		if (filterRequest.getWithoutTasks()) {
			sessionSchedule = getStaffWithoutTasks(sessionSchedule);
		}
		return sessionSchedule;
	}


	public int getNumberOfPages(LocalDate startDate, LocalDate endDate) {
		int staffNum = schedulerRepository.getNumberOfStaff(startDate, endDate);
		int numPages = 0;
		System.out.println("Staff num: " + staffNum);
		if (staffNum % Constants.STAFF_PER_PAGE == 0) {
			numPages = staffNum / Constants.STAFF_PER_PAGE;
		}
		else {
			numPages = staffNum / Constants.STAFF_PER_PAGE + 1;
		}
		
		System.out.println("Pages: " + numPages);
		
		return numPages;
	}

}
