package com.ascendaz.roster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.service.SchedulerService;


@Controller("schedulerController")
public class SchedulerController {
	
	@Autowired
	private SchedulerService schedulerService;
	
	@RequestMapping(value = "scheduler")
	public String configPage(Model model){
		
		System.out.println("Scheduler Controller");
		
		List<Schedule> schedule = schedulerService.processRules();
		
		return "scheduler";
	}
	
}
