package com.ascendaz.roster.service;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascendaz.roster.engine.RosterEngine;
import com.ascendaz.roster.exception.RosterEngineException;
import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.config.Rule;
import com.ascendaz.roster.repository.SchedulerRepository;

@Service("scheduleService")
public class SchedulerService {

	@Autowired
	private SchedulerRepository schedulerRepository;
	
	public List<Schedule> processRules() {
		
		List<TaskProfile> tasksProfile = schedulerRepository.getTasks();
		List<Staff> staff = schedulerRepository.getStaff();
		List<Rule> rules = schedulerRepository.getSelectedRules();
		
		RosterEngine engine = new RosterEngine(tasksProfile, staff, rules);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse("01/08/2015");
			endDate = sdf.parse("08/08/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Schedule> schedule = null;
		try {
			schedule = engine.processRules(startDate, endDate);
		} catch (RosterEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println();
		System.out.println("SCHEDULER:");
		int i = 0;
		for (Schedule s: schedule) {
			System.out.println(i + ". " + s);
			++i;
		}
		
		return schedule;
	}

}
