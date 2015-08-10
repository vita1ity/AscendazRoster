
package com.ascendaz.roster.engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ascendaz.roster.exception.RosterEngineException;
import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.config.Rule;

public class RosterEngineMulti {
	
	private  CopyOnWriteArrayList<TaskProfile> threadSafeTaskList = new CopyOnWriteArrayList<TaskProfile>();
	private  CopyOnWriteArrayList<Staff> threadSafeStaffList = new CopyOnWriteArrayList<Staff>();
	private  CopyOnWriteArrayList<Rule> threadSafeRuleList = new CopyOnWriteArrayList<Rule>();
	private final Shift leaveShift;
	private final Shift dayOffShift;
	/*private List<TaskProfile> taskList;
	private List<Staff> staffList;
	private List<Rule> ruleList;*/
	
	public RosterEngineMulti(List<TaskProfile> taskList, List<Staff> staffList, List<Rule> ruleList, 
			Shift leaveShift, Shift dayOffShift) {
		super();
		/*this.taskList = taskList;
		this.staffList = staffList;
		this.ruleList = ruleList;*/
		this.threadSafeTaskList.addAll(taskList);
		this.threadSafeStaffList.addAll(staffList);
		this.threadSafeRuleList.addAll(ruleList);
		this.leaveShift = leaveShift;
		this.dayOffShift = dayOffShift;
	}

	//TODO
	//@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Schedule> processRules(Date startDate, Date endDate) throws RosterEngineException, InterruptedException {
		
		long start = System.currentTimeMillis();
		
		List<Schedule> schedule = new ArrayList<Schedule>();
		
		//sort staff by salary
		Collections.sort(threadSafeStaffList, Staff.SALARY_COMPARATOR);
		//sort rules by priority
		Collections.sort(threadSafeRuleList);
		
		Thread thread = null;
		EngineThread engineThread = null;
		List<Thread> threads = new ArrayList<Thread>();
		List<EngineThread> engineThreads = new ArrayList<EngineThread>();
		Date currentDate = startDate;
		while (currentDate.compareTo(endDate) <= 0) {
			
			
			engineThread = new EngineThread(threadSafeTaskList, threadSafeStaffList, threadSafeRuleList, 
					leaveShift, dayOffShift, (Date)currentDate.clone());
			thread = new Thread(engineThread);
			threads.add(thread);
			engineThreads.add(engineThread);
			thread.start();
			
			//get next day
			Calendar c = Calendar.getInstance(); 
			c.setTime(currentDate); 
			c.add(Calendar.DATE, 1);
			currentDate = c.getTime();
		}//days list end
		for (Thread t: threads) {
			System.out.println("Thread joined: " + t.getName());
			t.join();
		}
		//List<Schedule> daySchedule = engineThreads.get(0).getSchedule();
		//schedule.addAll(daySchedule);
		
		for (EngineThread et: engineThreads) {
			List<Schedule> daySchedule = et.getSchedule();
			System.out.println(et.getLog());
			schedule.addAll(daySchedule);
		}
		
		long end = System.currentTimeMillis();
		System.out.println("PROCESS FINISHED. Time: " + (end - start));
		
		return schedule;
	}


	
	
}
