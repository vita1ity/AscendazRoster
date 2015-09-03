
package com.ascendaz.roster.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.ascendaz.roster.exception.RosterEngineException;
import com.ascendaz.roster.exception.RosterExceptionListener;
import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.config.Rule;

public class RosterEngineMulti {
	
	static Logger logger = LoggerFactory.getLogger(RosterEngineMulti.class);
	
	private RosterExceptionListener listener = new RosterExceptionListener();
	
	private  CopyOnWriteArrayList<TaskProfile> threadSafeTaskList = new CopyOnWriteArrayList<TaskProfile>();
	private  CopyOnWriteArrayList<Staff> threadSafeStaffList = new CopyOnWriteArrayList<Staff>();
	private  CopyOnWriteArrayList<Rule> threadSafeRuleList = new CopyOnWriteArrayList<Rule>();
	private final Shift leaveShift;
	private final Shift dayOffShift;
	
	public RosterEngineMulti(List<TaskProfile> taskList, List<Staff> staffList, List<Rule> ruleList, 
			Shift leaveShift, Shift dayOffShift) throws RosterEngineException {
		super();
		if (leaveShift == null) {
			throw new RosterEngineException("Leave shift shouln't be null.\n "
					+ "Please check database set up for for leave shift definition");
		}
		if (dayOffShift == null) {
			throw new RosterEngineException("Day off shift shouln't be null.\n "
					+ "Please check database set up for for Day off shift definition");
		}
		if (staffList == null || staffList.size() == 0) {
			throw new RosterEngineException("Staff list is empty.\n "
					+ "Please fill database with staff items to complete roster process");
		}
		if (taskList == null || taskList.size() == 0) {
			throw new RosterEngineException("Task profile list is empty.\n "
					+ "Please fill database with task profile items to complete roster process");
		}
		if (ruleList == null || ruleList.size() == 0) {
			throw new RosterEngineException("Rules list is empty.\n "
					+ "Please fill database with rules to complete roster process");
		}
		this.threadSafeTaskList.addAll(taskList);
		this.threadSafeStaffList.addAll(staffList);
		this.threadSafeRuleList.addAll(ruleList);
		this.leaveShift = leaveShift;
		this.dayOffShift = dayOffShift;
	}
	public List<Schedule> processRules(LocalDate startDate, LocalDate endDate, boolean considerSalary) 
			throws RosterEngineException {
		
		MDC.put("logFileName", "all-threads");
		
		long start = System.currentTimeMillis();
		
		List<Schedule> schedule = new ArrayList<Schedule>();
		
		if (considerSalary) {
			//sort staff by salary
			Collections.sort(threadSafeStaffList, Staff.SALARY_COMPARATOR);
		}
		//sort rules by priority
		Collections.sort(threadSafeRuleList);
		
		Thread thread = null;
		EngineThread engineThread = null;
		List<Thread> threads = new ArrayList<Thread>();
		List<EngineThread> engineThreads = new ArrayList<EngineThread>();
		LocalDate currentDate = startDate;
		int i = 1;
		while (currentDate.compareTo(endDate) <= 0) {
			
			
			engineThread = new EngineThread(threadSafeTaskList, threadSafeStaffList, threadSafeRuleList, 
					leaveShift, dayOffShift, currentDate, listener);
			engineThread.setNumber(i);
			thread = new Thread(engineThread);
			threads.add(thread);
			engineThreads.add(engineThread);
	
			thread.start();
			i++;
			//get next day
			currentDate = currentDate.plusDays(1);
			
		}//days list end
		for (Thread t: threads) {
			
			try {
				t.join();
			} catch (InterruptedException e) {
				// Restore the interrupted status
		        Thread.currentThread().interrupt();
		    }
		}
		if (listener.isException()) {
			throw new RosterEngineException(listener.getEx().getMessage());
		}
		
		for (EngineThread et: engineThreads) {
			List<Schedule> daySchedule = et.getSchedule();
			//System.out.println(et.getLog());
			schedule.addAll(daySchedule);
		}
		
		long end = System.currentTimeMillis();
		System.out.println("PROCESS FINISHED. Time: " + (end - start) / 1000 + "s");
		logger.debug("PROCESS FINISHED. Time: " + (end - start) / 1000 + "s");
		MDC.remove("logFileName");
		
		return schedule;
	}

	
}
