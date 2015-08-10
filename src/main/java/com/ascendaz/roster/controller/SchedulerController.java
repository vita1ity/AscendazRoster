package com.ascendaz.roster.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.service.SchedulerService;


@Controller("schedulerController")
public class SchedulerController {
	
	@Autowired
	private SchedulerService schedulerService;
	
	@RequestMapping(value = "scheduler")
	public String configPage(Model model){
		
		System.out.println("Scheduler Controller");
		
		//List<Schedule> schedule = schedulerService.processRules();
		
		return "scheduler";
	}
	
	@RequestMapping(value = "/scheduler/run-engine", method = RequestMethod.POST)
	public @ResponseBody List<Schedule> runEngine(@RequestParam(value="startDate", required=true) String startDate, 
			@RequestParam(value="endDate", required=true) String endDate) throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date startD = formatter.parse(startDate);
		Date endD = formatter.parse(endDate);
		
		List<Schedule> schedule = schedulerService.processRules(startD, endD);
		
		return schedule;
	}
	
	//TODO remove only for testing
	@RequestMapping(value = "/scheduler/run-engine", method = RequestMethod.GET)
	public @ResponseBody List<Schedule> runEngine() throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date startD = formatter.parse("02/08/2015");
		Date endD = formatter.parse("08/08/2015");
		
		List<Schedule> schedule = schedulerService.processRules(startD, endD);
		
		return schedule;
	}
	
	
}
