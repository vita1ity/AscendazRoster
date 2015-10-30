package com.ascendaz.roster.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ascendaz.roster.exception.ErrorInfo;
import com.ascendaz.roster.exception.RosterEngineException;
import com.ascendaz.roster.model.FilterRequestJson;
import com.ascendaz.roster.model.ScheduleResponse;
import com.ascendaz.roster.model.attributes.Location;
import com.ascendaz.roster.service.SchedulerService;


@Controller("schedulerController")
public class SchedulerController {
	
	@Autowired
	private SchedulerService schedulerService;
	
	@RequestMapping(value = "scheduler")
	public String schedulerPage(Model model, HttpSession session){
		
		System.out.println("Scheduler Controller");
		LocalDate now = new LocalDate();
		LocalDate monday = now.withDayOfWeek(DateTimeConstants.MONDAY);
		LocalDate tuesday = now.withDayOfWeek(DateTimeConstants.TUESDAY);
		LocalDate wednesday = now.withDayOfWeek(DateTimeConstants.WEDNESDAY);
		LocalDate thursday = now.withDayOfWeek(DateTimeConstants.THURSDAY);
		LocalDate friday = now.withDayOfWeek(DateTimeConstants.FRIDAY);
		LocalDate saturday = now.withDayOfWeek(DateTimeConstants.SATURDAY);
		LocalDate sunday = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		
		String[] daysOfWeek = new String[7];
		daysOfWeek[0] = monday.toString("MMM dd E");
		daysOfWeek[1] = tuesday.toString("MMM dd E");
		daysOfWeek[2] = wednesday.toString("MMM dd E");
		daysOfWeek[3] = thursday.toString("MMM dd E");
		daysOfWeek[4] = friday.toString("MMM dd E");
		daysOfWeek[5] = saturday.toString("MMM dd E");
		daysOfWeek[6] = sunday.toString("MMM dd E");
		
		List<ScheduleResponse> schedule = schedulerService.getScheduleForPeriod(monday, sunday, 1);
		
		List<Location> locationList = schedulerService.getAllLocations();
		model.addAttribute("locationList", locationList);
		
		model.addAttribute("schedule", schedule);
		session.setAttribute("schedule", schedule);
		
		int numberOfPages = schedulerService.getNumberOfPages(monday, sunday);
		model.addAttribute("numberOfPages", numberOfPages);
		model.addAttribute("startDisplayPage", 1);
		model.addAttribute("currentPage", 1);
		
		model.addAttribute("currentDay", now.toString("MMM dd E"));
		model.addAttribute("startDate", monday);
		model.addAttribute("endDate", sunday);
		model.addAttribute("daysOfWeek", daysOfWeek);
		
		return "scheduler";
	}
	
	@RequestMapping(value = "/scheduler/run-engine", method = RequestMethod.POST)
	public @ResponseBody List<ScheduleResponse> runEngine(@RequestParam(value="startDate", required=true) String startDate, 
			@RequestParam(value="endDate", required=true) String endDate, @RequestParam(value="considerSalary", required=true) boolean considerSalary, HttpSession session) 
					throws ParseException, InterruptedException, RosterEngineException {
		
		DateTimeFormatter df = DateTimeFormat.forPattern("dd/MM/yyyy");
		LocalDate startD = LocalDate.parse(startDate, df);
		LocalDate endD = LocalDate.parse(endDate, df);

		 List<ScheduleResponse> schedule = schedulerService.processRules(startD, endD, considerSalary);
		
		session.removeAttribute("schedule");
		session.setAttribute("schedule", schedule);
		

		return schedule;
	}
	
	
	@RequestMapping(value = "/scheduler/approve-schedule", method = RequestMethod.GET)
	public @ResponseBody List<ScheduleResponse> approveSchedule(HttpSession session) {
		@SuppressWarnings("unchecked")
		List<ScheduleResponse> sessionSchedule = (List<ScheduleResponse>)session.getAttribute("schedule");
		if (sessionSchedule == null) {
			return null;
		}
		
		List<ScheduleResponse> modifiedSchedule = schedulerService.setApprovedStatus(sessionSchedule);
		session.removeAttribute("schedule");
		session.setAttribute("schedule", modifiedSchedule);
		return modifiedSchedule;
		
	}
	
	
	
	@RequestMapping(value = "/scheduler/get-schedule", method = RequestMethod.POST)
	public @ResponseBody List<ScheduleResponse> getSchedule(@RequestParam(value="startDate", required=true) String startDate, 
			@RequestParam(value="endDate", required=true) String endDate, @RequestParam(value="page", required=true) int page, HttpSession session){
		DateTimeFormatter df = DateTimeFormat.forPattern("dd/MM/yyyy");
		LocalDate startD = LocalDate.parse(startDate, df);
		LocalDate endD = LocalDate.parse(endDate, df);

		System.out.println("get schedule. page: " + page);
		
		List<ScheduleResponse> schedule = schedulerService.getScheduleForPeriod(startD, endD, page);
		session.removeAttribute("schedule");
		session.setAttribute("schedule", schedule);
		
		return schedule;
	}
	
	/*@RequestMapping(value = "/scheduler/staff-without-tasks", method = RequestMethod.GET)
	public @ResponseBody List<ScheduleResponse> getStaffWithoutTasks (HttpSession session){
		@SuppressWarnings("unchecked")
		List<ScheduleResponse> sessionSchedule = (List<ScheduleResponse>)session.getAttribute("schedule");
		
		List<ScheduleResponse> schedule = schedulerService.getStaffWithoutTasks(sessionSchedule);
		
		return schedule;
	}
	
	@RequestMapping(value = "/scheduler/filter-locations", method = RequestMethod.POST)
	public @ResponseBody List<ScheduleResponse> saveRules(@RequestBody List<String> locations, HttpSession session) {
		
		System.out.println("FILTER LOCATIONS");
		for (String location: locations) {
			System.out.println(location);
		}
		@SuppressWarnings("unchecked")
		List<ScheduleResponse> sessionSchedule = (List<ScheduleResponse>)session.getAttribute("schedule");
		List<ScheduleResponse> schedule = schedulerService.getScheduleForLocations(sessionSchedule, locations);
		
		return schedule;
	}
	
	@RequestMapping(value = "/scheduler/rules-violated-tasks", method = RequestMethod.GET)
	public @ResponseBody List<ScheduleResponse> getRulesViolatedTasks (HttpSession session){
		@SuppressWarnings("unchecked")
		List<ScheduleResponse> sessionSchedule = (List<ScheduleResponse>)session.getAttribute("schedule");
		
		List<ScheduleResponse> schedule = schedulerService.getRulesViolatedTasks(sessionSchedule);
		
		return schedule;
	}
	
	@RequestMapping(value = "/scheduler/leaves-tasks", method = RequestMethod.GET)
	public @ResponseBody List<ScheduleResponse> getLeavesTasks (HttpSession session){
		@SuppressWarnings("unchecked")
		List<ScheduleResponse> sessionSchedule = (List<ScheduleResponse>)session.getAttribute("schedule");
		
		List<ScheduleResponse> schedule = schedulerService.getLeavesTasks(sessionSchedule);
		
		return schedule;
	}*/
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/scheduler/apply-filters", method = RequestMethod.POST)
	public @ResponseBody List<ScheduleResponse> applyFilters(@RequestBody FilterRequestJson filterRequest, HttpSession session){

		System.out.println("leaveTasks: " + filterRequest.getLeaveTasks());
		System.out.println("locationTasks: " + filterRequest.getLocationTasks());
		System.out.println("withoutTasks: " + filterRequest.getWithoutTasks());
		System.out.println("rulesViolated: " + filterRequest.getRulesViolated());
		System.out.println("locations: " + filterRequest.getLocations());
		List<ScheduleResponse> sessionSchedule = (List<ScheduleResponse>)session.getAttribute("schedule");
		List<ScheduleResponse> filteredSchedule = schedulerService.applyFilters(filterRequest, sessionSchedule);
		
		return filteredSchedule;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RosterEngineException.class)
	@ResponseBody ErrorInfo handleBadRequest(HttpServletRequest req, Exception ex) {
		System.out.println("EXCEPTION!!!!");
	    return new ErrorInfo(req.getRequestURL().toString(), ex);
	} 
		
	
}
