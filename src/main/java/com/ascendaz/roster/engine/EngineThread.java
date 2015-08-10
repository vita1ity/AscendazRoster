package com.ascendaz.roster.engine;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.ascendaz.roster.exception.RosterEngineException;
import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.model.ShiftPattern;
import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.StaffLeave;
import com.ascendaz.roster.model.StaffLocation;
import com.ascendaz.roster.model.StaffShift;
import com.ascendaz.roster.model.Task;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.attributes.Attribute;
import com.ascendaz.roster.model.attributes.CustomEquality;
import com.ascendaz.roster.model.attributes.DayOfWeek;
import com.ascendaz.roster.model.attributes.Expiring;
import com.ascendaz.roster.model.attributes.Location;
import com.ascendaz.roster.model.attributes.Range;
import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.config.Criteria;
import com.ascendaz.roster.model.config.Reference;
import com.ascendaz.roster.model.config.Rule;
import com.ascendaz.roster.model.config.RuleType;
import com.ascendaz.roster.model.config.SetupOption;

public class EngineThread implements Runnable{
	
	private  CopyOnWriteArrayList<TaskProfile> taskList = null;
	private  CopyOnWriteArrayList<Staff> staffList = null;
	private  CopyOnWriteArrayList<Rule> ruleList = null;
	private Date currentDate;
	private final Shift leaveShift;
	private final Shift dayOffShift;
	private List<Schedule> schedule = new ArrayList<Schedule>();
	
	private String log = "";
	
	public EngineThread(CopyOnWriteArrayList<TaskProfile> threadSafeTaskList,
			CopyOnWriteArrayList<Staff> threadSafeStaffList, CopyOnWriteArrayList<Rule> threadSafeRuleList,
			Shift leaveShift, Shift dayOffShift, Date currentDate) {
		super();
		this.taskList = threadSafeTaskList;
		this.staffList = threadSafeStaffList;
		this.ruleList = threadSafeRuleList;
		this.leaveShift = leaveShift;
		this.dayOffShift = dayOffShift;
		this.currentDate = currentDate;
	}

	

	public List<Schedule> getSchedule() {
		return this.schedule;
	}

	public String getLog() {
		return log;
	}
	
	@Override
	public void run() {
		
		//System.out.println("THREAD STARTED: " + Thread.currentThread().getName());
		log += "THREAD STARTED: " + Thread.currentThread().getName() + "\n";
		
		Schedule sch = null;
		List<Schedule> softRuleViolated = null;
		List<Integer> violatedStaffIndexes = null;
		
		int numStaffLeft = 0;
		
		//staff
		LocalDate effectLocalDate = null;
		Date startEffectDate = null;
		Date endExpireDate = null;
		
		Calendar current = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
		
		boolean [] isBusyStaff = new boolean[staffList.size()];
		for (boolean b: isBusyStaff) {
			b = false;
		}
		
		LocalDate currentLocalDate = new LocalDate(currentDate);
		current.setTime(currentDate);
		
		/*System.out.println();
		System.out.println("========================== CURRENT DATE: " + df.format(currentDate) + " ============================");
		System.out.println();*/
		
		log +="\n========================== CURRENT DATE: " + df.format(currentDate) + " ============================ \n\n";
		
		
		//TODO remove corying list 
		//List<Staff> candidatesForCurrentDay = new ArrayList<Staff>(staffList);
		/*for (Staff staff: staffList) {
			staff.setBusy(false);
		}*/
		/*for (TaskProfile profile: taskList) {
			profile.setNeedsToBePerformed(true);
		}*/
		
		
		
     /*   while(failSafeIterator.hasNext()){
            System.out.printf("Read from CopyOnWriteArrayList : %s %n", failSafeIterator.next());
            failSafeIterator.remove(); //not supported in CopyOnWriteArrayList in Java
        }*/
		
		Iterator<TaskProfile> taskIterator = taskList.iterator();
		Task task = null;
		TaskProfile taskProfile = null;
		nextTask: while(taskIterator.hasNext()){
		//nextTask: for (TaskProfile taskProfile: taskList) {
			/*if (!taskProfile.isNeedsToBePerformed()) {
				continue;
			}*/
			taskProfile = taskIterator.next();
			/*System.out.println();
			System.out.println("------------------Task: " + taskProfile + " --------------------");
			System.out.println();*/
			log += "\n------------------Task: " + taskProfile + " --------------------\n\n";
			
			softRuleViolated = new ArrayList<Schedule>();
			violatedStaffIndexes = new ArrayList<Integer>();
			
			task = taskProfile.getTask();
			int numOfStaffNeeded = task.getHeadcount();
			numStaffLeft = numOfStaffNeeded;
			
			//check if Task is valid for this date
			startEffectDate = task.getStartDate();
			endExpireDate = task.getEndDate();
			
			if (startEffectDate.compareTo(currentDate) >= 0 || endExpireDate.compareTo(currentDate) <= 0) {
				if (!df.format(startEffectDate).equals(df.format(currentDate))) {
					/*System.out.println("Task does not need to be peformed for current date. Current date: " + df.format(currentDate) +
							" task dates: from " + df.format(startEffectDate) + " to " + df.format(endExpireDate));*/
					log += "Task does not need to be peformed for current date. Current date: " + df.format(currentDate) +
							" task dates: from " + df.format(startEffectDate) + " to " + df.format(endExpireDate) + "\n";
					
					//taskList.remove(task);
					continue;
				}
			}
			List<DayOfWeek> daysOfWeek= task.getDayOfWeekSet();
			
			
			//check if task should be performed in current day of week
			boolean isPerformedForCurrentDayOfWeek = false;
			//0 is equal to all days of week
			if (daysOfWeek.size() == 1 && daysOfWeek.get(0).getNumber() == 0) {
				
				//System.out.println("Task should be performed for the whole week");
				log += "Task should be performed for the whole week\n";
						
				isPerformedForCurrentDayOfWeek = true;
			}
			if (!isPerformedForCurrentDayOfWeek) {
				
				
				int dayOfWeek = current.get(Calendar.DAY_OF_WEEK);
				for (DayOfWeek day: daysOfWeek) {
					if (day.getNumber() == dayOfWeek) {
						//System.out.println("Task should be performed for the current day: " + dayOfWeek);
						log += "Task should be performed for the current day: " + dayOfWeek + "\n";
						isPerformedForCurrentDayOfWeek = true;
						break;
					}
					else {
						//System.out.println("Task should be performed for the day: " + day);
						log += "Task should be performed for the day: " + day + "\n";
					}
				}
				if (!isPerformedForCurrentDayOfWeek) {
					//System.out.println("Task shouldn't be performed for the current day: " + dayOfWeek);
					log += "Task shouldn't be performed for the current day: " + dayOfWeek + "\n";
					//taskList.remove(task);
					continue;
				}
			}
			
			
			/*Date effectDate = null;
			Date expireDate = null;*/
			
			Iterator<Staff> staffIterator = staffList.iterator();
			Staff staff = null;
			//staff: for (Staff staff: new ArrayList<Staff>(candidatesForCurrentDay)) {
			int index = -1;
			staff: while (staffIterator.hasNext()) {
			//staff: for (Staff staff: staffList) {
				++index;
				staff = staffIterator.next();
				if (isBusyStaff[index]) {
					continue;
				}
				/*if (staff.isBusy()) {
					continue;
				}*/
				
				/*System.out.println();
				System.out.println("+++++++++++++++++++++++" + staff + "++++++++++++++++++++++++++++");
				System.out.println();*/
				
				log += "\n\n+++++++++++++++++++++++" + staff + "++++++++++++++++++++++++++++\n";
				
				char shiftForTheDay = '0';
				
				
				Set<StaffShift> staffShieftSet = staff.getStaffShiftSet();
				for (StaffShift staffShift: staffShieftSet) {
					
					startEffectDate = staffShift.getEffectiveDate();
					endExpireDate = staffShift.getExpireDate();
					
					if (staffShift.checkExpired(currentDate)) {
						
						//System.out.println("Staff shieft is out of date. Current date: " + df.format(currentDate) +
						//		" shift effective date " + df.format(startEffectDate) + " expire " + df.format(endExpireDate));
						log += "Staff shieft is out of date. Current date: " + df.format(currentDate) +
								" shift effective date " + df.format(startEffectDate) + " expire " + df.format(endExpireDate) + "\n";
						continue;
					}
					/*effectDate = staffShift.getEffectiveDate();
					expireDate = staffShift.getExpireDate();
					if (effectDate.compareTo(currentDate) > 0 || expireDate.compareTo(currentDate) < 0) {
						System.out.println("Staff shieft is out of date. Current date: " + df.format(currentDate) +
								" shieft effective date " + df.format(effectDate) + " expire " + df.format(expireDate));
						continue;
					}*/
					else {
						ShiftPattern shiftPattern = staffShift.getShiftPattern();
						effectLocalDate = new LocalDate(startEffectDate);
						
						int days = Days.daysBetween(effectLocalDate, currentLocalDate).getDays();
						String pattern = shiftPattern.getName();
						int numOfDay = days % pattern.length();
						
						shiftForTheDay = pattern.charAt(numOfDay);
						if (shiftForTheDay == 'O') {
							
							log += "Staff has day off in the date: " + df.format(currentDate) + "\n";
							//System.out.println("Staff has day off in the date: " + df.format(currentDate));
							/*Shift shift = new Shift();
							shift.setShift("OFF");
							shift.setShiftLetter("O");*/
							
							sch = new Schedule(null, staff, currentDate, dayOffShift, "Draft", false);
							schedule.add(sch);
							isBusyStaff[index] = true;
							//staff.setBusy(true);
							//candidatesForCurrentDay.remove(staff);
							continue staff;
						}
						
						break;
					}
				}
				
				Set<StaffLeave> staffLeaveSet = staff.getStaffLeaveSet();
				
				for (StaffLeave staffLeave: staffLeaveSet) {
					
					cal2.setTime(staffLeave.getDate());
					boolean sameDay = current.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
					                  current.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
					/*System.out.println(current.get(Calendar.YEAR));
					System.out.println(cal2.get(Calendar.YEAR));
					System.out.println(current.get(Calendar.MONTH));
					System.out.println(cal2.get(Calendar.MONTH));
					System.out.println(current.get(Calendar.DAY_OF_MONTH));
					System.out.println(cal2.get(Calendar.DAY_OF_MONTH));
					System.out.println(current.get(Calendar.DAY_OF_YEAR));
					System.out.println(cal2.get(Calendar.DAY_OF_YEAR));*/
					
					if (sameDay) {
					
					//if (staffLeave.getDate().equals(currentDate)) {
						//System.out.println("Staff has annual leave today");
						log += "Staff has annual leave today\n";
						
						if (staffLeave.getStatus().equalsIgnoreCase("approved")) {
							
							log += "leave is approved\n";
							//System.out.println("leave is approved");
							
							/*Shift shift = new Shift();
							shift.setShift("Annual Leave");
							shift.setShiftLetter("L");*/
							sch = new Schedule(null, staff, currentDate, leaveShift, "Draft", false);
							schedule.add(sch);
							isBusyStaff[index] = true;
							//staff.setBusy(true);
							//candidatesForCurrentDay.remove(staff);
							continue staff;
						}
						break;
					}
				}
				
				
				//RULES CHECKING
				boolean violatesSoftRule = false;
				Iterator<Rule> ruleIterator = ruleList.iterator();
				Rule rule = null;
				while (ruleIterator.hasNext()) {
				//for (Rule rule: ruleList) {
					rule = ruleIterator.next();
					SetupOption option = rule.getSetupOption();
					Reference reference = rule.getReference();
					Criteria criteria = rule.getCriteria();
					String staffAttributeName = null;
					String taskAttributeName = null;
					synchronized(this.getClass()) {
						staffAttributeName = option.getAttributeName();
						taskAttributeName = reference.getAttributeName();
					}
					log += "Rule: " + staffAttributeName + " " + criteria + " " + taskAttributeName + "\n";
					//System.out.println("Rule: " + staffAttributeName + " " + criteria + " " + taskAttributeName);
					
					//get attribute with given name
					Class<?> staffClass = staff.getClass();
					Class<?> taskClass = taskProfile.getClass();
					
					Field staffField;
					Field taskField;
					try {
						staffField = staffClass.getDeclaredField(staffAttributeName);
						taskField = taskClass.getDeclaredField(taskAttributeName);
						
						staffField.setAccessible(true);
						taskField.setAccessible(true);
						
						Object staffObject = staffField.get(staff);
						Object taskObject = taskField.get(taskProfile);
						
						log += "STAFF OBJECT: " + staffObject + ", TASK OBJECT: " + taskObject + "\n";
						//System.out.println("STAFF OBJECT: " + staffObject + ", TASK OBJECT: " + taskObject);
						
						//skip this rule if task object doesn't have it specified
						if (taskObject == null) {
							continue;
						}
						
						
						//Shift attributes
						else if (taskObject instanceof Shift) {
							/*if (!(taskObject instanceof Shift)) {
								throw new RosterEngineException("Staff rule type is Shift. Expected type of task is Shift. Current type: " 
										+ taskObject.getClass().getName());
							}
							else {*/
								 
							log += "staff and task type is Shift\n";
								//System.out.println("staff and task type is Shift");
								
								String taskShift = taskProfile.getShift().getShiftLetter();
								char taskShiftCharacter = taskShift.charAt(0);
								log += "staff shift: " + shiftForTheDay + "\n";
								//System.out.println("staff shift: " + shiftForTheDay);
								//shift supports only equals criteria
								
								if (!criteria.equals(Criteria.EQUAL)) {
									throw new RosterEngineException("Invalid rule: only EQUALS applies to the Shift rule");
								}
								else if (taskShiftCharacter == shiftForTheDay) {
									log += "Shift rule approved!\n";
									//System.out.println("Shift rule approved!");
									continue;
								}
								else {
									log += "Shift rule not approved\n";
									//System.out.println("Shift rule not approved");
									continue staff;
								}
								
							//}
						}
						//Location attribute. 
						else if (taskObject instanceof Location) {
							
							log += "Location rule\n";
							//System.out.println("Location rule");
							
							if (!(staffObject instanceof Collection)) {
								throw new RosterEngineException("Task type is Location. Staff should be of type Collection<Location>");
							}
							else {
								
								String setupOption = option.getSetupOption();
								
								List<StaffLocation> staffLocations = new ArrayList<StaffLocation>();
								
								//Assigned or restricted locations
								Collection staffColl = (Collection)staffObject;
								for (Object obj: staffColl) {
									if (!(obj instanceof StaffLocation)) {
										throw new RosterEngineException("Task type is Location. Staff object should be of type StaffLocation");
									}
									else {
										StaffLocation staffLocation = (StaffLocation)obj;
										if (setupOption.contains("Assigned") && staffLocation.getType().equals("Assigned")) {
											staffLocations.add(staffLocation);
										}
										else if (setupOption.contains("Restricted") && staffLocation.getType().equals("Restricted")) {
											staffLocations.add(staffLocation);
										}
									}
								}
								staffObject = staffLocations;
								
							}
						}
						
						
						if (processCriteria(criteria, taskObject, staffObject, currentDate)) {
							log += "Staff satisfies the rule\n";
							//System.out.println("Staff satisfies the rule");
							continue;
						}
						
						if (softRuleViolated.size() < numStaffLeft && rule.getType().equals(RuleType.Soft)) {
							log += "Staff violates soft rule\n";
							//System.out.println("Staff violates soft rule");
							violatesSoftRule = true;
							continue;
						}
						//HARD rule
						else {
							//check next staff
							log += "Staff doesn't satisfy the rule\n";
							//System.out.println("Staff doesn't satisfy the rule");
							continue staff;
						}
						
					
					} catch (NoSuchFieldException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (RosterEngineException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}//rule list end
				
				
				//soft rule violated but candidate founded
				if (violatesSoftRule && numStaffLeft > softRuleViolated.size()) {
					//add candidate as soft violation variant for this task and day
					/*System.out.println("Schedule with soft rule violation added:");
					System.out.println("Task: " + taskProfile);
					System.out.println("Staff: " + staff);
					System.out.println("Day: " + currentDate);
					System.out.println("Shift: " + taskProfile.getShift());*/
					
					log += "Schedule with soft rule violation added:\n";
					log += "Task: " + taskProfile + "\n";
					log += "Staff: " + staff + "\n";
					log += "Day: " + currentDate + "\n";
					log += "Shift: " + taskProfile.getShift() + "\n";
					
					violatedStaffIndexes.add(index); 
					Schedule violatedSchedule = new Schedule(taskProfile, staff, currentDate, taskProfile.getShift(), "Draft", true);
					softRuleViolated.add(violatedSchedule);
				}
				//candidate satisfies all rules 
				else {
					
					/*System.out.println("Schedule added:");
					System.out.println("Task: " + taskProfile);
					System.out.println("Staff: " + staff);
					System.out.println("Day: " + currentDate);
					System.out.println("Shift: " + taskProfile.getShift());*/
					
					log += "Schedule  added:\n";
					log += "Task: " + taskProfile + "\n";
					log += "Staff: " + staff + "\n";
					log += "Day: " + currentDate + "\n";
					log += "Shift: " + taskProfile.getShift() + "\n";
					
					sch = new Schedule(taskProfile, staff, currentDate, taskProfile.getShift(), "Draft", false);
					schedule.add(sch);
					//candidatesForCurrentDay.remove(staff);
					isBusyStaff[index] = true;
					//staff.setBusy(true);
					if (--numStaffLeft == 0) {
						continue nextTask;
					}
					else {
						continue;
					}
				}
				
				
			}//staff list end
			
			//there are no candidates for the task
			if (softRuleViolated.size() != 0) {
				int i = 0;
				for (Schedule violatedSch: softRuleViolated) {
					/*System.out.println("Violated Schedule added:");
					System.out.println("Task: " + taskProfile);
					System.out.println("Staff: " + violatedSch.getStaff());
					System.out.println("Day: " + currentDate);
					System.out.println("Shift: " + taskProfile.getShift());*/
					
					log += "Violated Schedule added:\n";
					log += "Task: " + taskProfile + "\n";
					log += "Staff: " + violatedSch.getStaff() + "\n";
					log += "Day: " + currentDate + "\n";
					log += "Shift: " + taskProfile.getShift() + "\n";
					
					schedule.add(violatedSch);
					isBusyStaff[violatedStaffIndexes.get(i)] = true;
					//violatedSch.getStaff().setBusy(true);
					++i;
					//candidatesForCurrentDay.remove(violatedSch.getStaff());
				}
				
			}
			else {
				/*System.out.println("There are no candidates for current task for the day");
				System.out.println("Task: " + taskProfile);
				System.out.println("Day: " + currentDate);*/
				
				log += "There are no candidates for current task for the day\n";
				log += "Task: " + taskProfile + "\n";
				log += "Day: " + currentDate + "\n";
			}
			
			
		}//task list end
		
		
	}
	
	//PRIVATE SUPPORT METHODS


	//return positive int if condition is sutisfied, 
	private boolean processCriteria(Criteria criteria, Object taskObject, Object staffObject, Date currentDate) throws RosterEngineException {
		
		//Collection<?> staffCollection = null;
		//Collection<?> taskCollection = null;
		Collection<Object> staffNotExpired = null;
		Expiring expStaffObj = null;
		boolean expiring = false;
		// Expiring case
		if (staffObject instanceof Collection) {
			//staffCollection = (Collection<?>) staffObject;
			//taskCollection = (Collection<?>) taskObject;
			
			staffNotExpired = new HashSet<Object>();
			
			//remove expired items
			
			for (Object staffObj: (Collection<?>)staffObject) {
				if (expiring == true || staffObj instanceof Expiring) {
					expiring = true;
					expStaffObj = (Expiring)staffObj;
					if (!expStaffObj.checkExpired(currentDate)) {
						log += "Not expired item added: " + expStaffObj.getValueObject() + "\n";
						//System.out.println("Not expired item added: " + expStaffObj.getValueObject());
						staffNotExpired.add(expStaffObj.getValueObject());
					}
					
				}
				else break;
			}
			
		}
		if (staffNotExpired != null && !staffNotExpired.isEmpty()) {
			if (staffNotExpired.size() == 1) {
				staffObject = expStaffObj.getValueObject();
			}
			else {
				staffObject = staffNotExpired;
			}
		}
		
		
		switch (criteria) {
		case EQUAL:
			log += "taskObject: " + taskObject + ", staffObject: " + staffObject + "\n";
			//System.out.println("taskObject: " + taskObject + ", staffObject: " + staffObject);
			if (taskObject instanceof CustomEquality && staffObject instanceof CustomEquality) {
				CustomEquality taskAttr = (CustomEquality)taskObject;
				CustomEquality staffAttr = (CustomEquality)staffObject;
				log += "taskAttr: " + taskAttr + ", staffAttr: " + staffAttr + "\n";
				if (staffAttr.isEqual(taskAttr)) {
				//if (taskObject.equals(staffObject)) {
					log += "Staff value: " + staffObject + " is equal to Task value: " + taskObject + "(CUSTOM EQUALITY)\n";
					//System.out.println("Staff value: " + staffObject + " is equal to Task value: " + taskObject);
					return true;
				}
			}
			else if (taskObject instanceof Attribute && staffObject instanceof Attribute) {
				Attribute taskAttr = (Attribute)taskObject;
				Attribute staffAttr = (Attribute)staffObject;
				if (staffAttr.getValue().equals(taskAttr.getValue())) {
				//if (taskObject.equals(staffObject)) {
					log += "Staff value: " + staffObject + " is equal to Task value: " + taskObject + "\n";
					//System.out.println("Staff value: " + staffObject + " is equal to Task value: " + taskObject);
					return true;
				}
			}
			return false;
		case NOT_EQUAL:
			log += "taskObject: " + taskObject + ", staffObject: " + staffObject + "\n";
			//System.out.println("taskObject: " + taskObject + ", staffObject: " + staffObject);
			if (taskObject instanceof Attribute && staffObject instanceof Attribute) {
				Attribute taskAttr = (Attribute)taskObject;
				Attribute staffAttr = (Attribute)staffObject;
				if (!staffAttr.getValue().equals(taskAttr.getValue())) {
					log += "Staff value: " + staffObject + " is not equal to Task value: " + taskObject + "\n";
					//System.out.println("Staff value: " + staffObject + " is not equal to Task value: " + taskObject);
					return true;
				}
				return false;
			}
			else {
				return true;
			}
		case CONTAINS:
			if (!(staffObject instanceof Collection)) {
				throw new RosterEngineException("CONTAINS rule is applicable only for Collection type. Staff type: " + staffObject.getClass().getName()
						+ ", task type: " + taskObject.getClass().getName());
			}
			Collection<?> staffCollection = (Collection)staffObject;
			if (taskObject instanceof Collection) {
				Collection<?> taskCollection = (Collection)taskObject;
				if (staffCollection.containsAll(taskCollection)) {
					log += "Staff collection contains all objects from Task collection\n";
					//System.out.println("Staff collection contains all objects from Task collection");
					return true;
				}
			}
			else {
				if (staffCollection.contains(taskObject)) {
					log += "Staff collection contains all objects from Task collection\n";
					//System.out.println("Staff collection contains all objects from Task collection");
					return true;
				}
			}
			
					
		
			return false;
		case IN_BETWEEN:
			if (taskObject instanceof Range) {
				if (!(staffObject instanceof Comparable<?>)) {
					throw new RosterEngineException("Task rule type is Range. Expected type of task is Comparable. Current type: " 
							+ staffObject.getClass().getName());
				}
				else {
					Range taskRange = (Range)taskObject;
					
					@SuppressWarnings("unchecked")
					Comparable<Number> staffNumber = (Comparable<Number>) staffObject;
					if (taskRange.checkInBetween(staffNumber)) {
						log += "Staff attribute is in between task range\n";
						//System.out.println("Staff attribute is in between task range");
						return true;
					}
				}
			}
			else {
				throw new RosterEngineException("IN_BETWEEN rule is applicable only for Range, Comparable type. Staff type: " + staffObject.getClass().getName()
						+ ", task type: " + taskObject.getClass().getName());
			}
			return false;
		case ATLEAST:
			if (!(staffObject instanceof Comparable && taskObject instanceof Comparable)) {
				throw new RosterEngineException("ATLEAST rule is applicable only for Comparable types. Staff type: " + staffObject.getClass().getName()
						+ ", task type: " + taskObject.getClass().getName());
			}
			else {
				Comparable compStaff = (Comparable) staffObject;
				Comparable compTask = (Comparable) taskObject;
				
				if (compStaff.compareTo(compTask) >= 0) {
					log += "Staff attribute is bigger than task attribute\n";
					//System.out.println("Staff attribute is bigger than task attribute");
					return true;
				}
			}
			return false;
		case ATMOST:
			if (!(staffObject instanceof Comparable && taskObject instanceof Comparable)) {
				throw new RosterEngineException("ATMOST rule is applicable only for Comparable types. Staff type: " + staffObject.getClass().getName()
						+ ", task type: " + taskObject.getClass().getName());
			}
			else {
				Comparable compStaff = (Comparable) staffObject;
				Comparable compTask = (Comparable) taskObject;
				
				if (compStaff.compareTo(compTask) <= 0) {
					log += "Staff attribute is smaller than task attribute\n";
					//System.out.println("Staff attribute is smaller than task attribute");
					return true;
				}
			}
			return false;
		default:
			return false;
		}
	}
	
	
	
}
