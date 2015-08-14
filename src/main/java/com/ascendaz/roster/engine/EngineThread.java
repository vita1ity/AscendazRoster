package com.ascendaz.roster.engine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.ascendaz.roster.exception.RosterEngineException;
import com.ascendaz.roster.exception.RosterExceptionListener;
import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.model.ShiftPattern;
import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.StaffLeave;
import com.ascendaz.roster.model.StaffLocation;
import com.ascendaz.roster.model.StaffShift;
import com.ascendaz.roster.model.Task;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.attributes.DayOfWeek;
import com.ascendaz.roster.model.attributes.Location;
import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.attributes.interfaces.Attribute;
import com.ascendaz.roster.model.attributes.interfaces.CustomEquality;
import com.ascendaz.roster.model.attributes.interfaces.Expiring;
import com.ascendaz.roster.model.attributes.interfaces.Range;
import com.ascendaz.roster.model.config.Criteria;
import com.ascendaz.roster.model.config.Reference;
import com.ascendaz.roster.model.config.Rule;
import com.ascendaz.roster.model.config.RuleType;
import com.ascendaz.roster.model.config.SetupOption;

public class EngineThread implements Runnable{
	
	private RosterExceptionListener listener;
	
	private CopyOnWriteArrayList<TaskProfile> taskList = null;
	private CopyOnWriteArrayList<Staff> staffList = null;
	private CopyOnWriteArrayList<Rule> ruleList = null;
	private LocalDate currentDate;
	private final Shift leaveShift;
	private final Shift dayOffShift;
	private List<Schedule> schedule = new ArrayList<Schedule>();
	
	private String log = "";
	
	public EngineThread(CopyOnWriteArrayList<TaskProfile> threadSafeTaskList,
			CopyOnWriteArrayList<Staff> threadSafeStaffList, CopyOnWriteArrayList<Rule> threadSafeRuleList,
			Shift leaveShift, Shift dayOffShift, LocalDate currentDate, RosterExceptionListener listener) {
		super();
		this.taskList = threadSafeTaskList;
		this.staffList = threadSafeStaffList;
		this.ruleList = threadSafeRuleList;
		this.leaveShift = leaveShift;
		this.dayOffShift = dayOffShift;
		this.currentDate = currentDate;
		this.listener = listener;
	}

	

	public List<Schedule> getSchedule() {
		return this.schedule;
	}

	public String getLog() {
		return log;
	}
	
	
	@Override
	public void run() {
		boolean error = true;
		while (error) {
			try {
				
				log += "THREAD STARTED: " + Thread.currentThread().getName() + "\n";
				
				Schedule sch = null;
				List<Schedule> softRuleViolated = null;
				List<Integer> violatedStaffIndexes = null;
				
				int numStaffLeft = 0;
				
				LocalDate effectDate = null;
				LocalDate expireDate = null;
				
				
				boolean [] isBusyStaff = new boolean[staffList.size()];
				for (boolean b: isBusyStaff) {
					b = false;
				}
				
				
				log +="\n========================== CURRENT DATE: " + currentDate + " ============================ \n\n";
				
				
				
				Iterator<TaskProfile> taskIterator = taskList.iterator();
				Task task = null;
				TaskProfile taskProfile = null;
				nextTask: while(taskIterator.hasNext()){
				
					taskProfile = taskIterator.next();
					
					log += "\n------------------Task: " + taskProfile + " --------------------\n\n";
					
					softRuleViolated = new ArrayList<Schedule>();
					violatedStaffIndexes = new ArrayList<Integer>();
					
					task = taskProfile.getTask();
					int numOfStaffNeeded = task.getHeadcount();
					numStaffLeft = numOfStaffNeeded;
					
					//check if Task is valid for this date
					effectDate = task.getStartDate();
					expireDate = task.getEndDate();
					
					if (effectDate.compareTo(currentDate) >= 0 || expireDate.compareTo(currentDate) <= 0) {
						
						log += "Task does not need to be peformed for current date. Current date: " + currentDate +
								" task dates: from " + effectDate + " to " + expireDate + "\n";
						continue;
						
					}
					List<DayOfWeek> daysOfWeek= task.getDayOfWeekSet();
					
					//check if task should be performed in current day of week
					boolean isPerformedForCurrentDayOfWeek = false;
					//0 is equal to all days of week
					if (daysOfWeek.size() == 1 && daysOfWeek.get(0).getNumber() == 0) {
						
						log += "Task should be performed for the whole week\n";
								
						isPerformedForCurrentDayOfWeek = true;
					}
					if (!isPerformedForCurrentDayOfWeek) {
						
						int dayOfWeek = currentDate.dayOfWeek().get();
						for (DayOfWeek day: daysOfWeek) {
							if (day.getNumber() == dayOfWeek) {
								
								log += "Task should be performed for the current day: " + dayOfWeek + "\n";
								isPerformedForCurrentDayOfWeek = true;
								break;
							}
							else {
								
								log += "Task should be performed for the day: " + day + "\n";
							}
						}
						if (!isPerformedForCurrentDayOfWeek) {
							
							log += "Task shouldn't be performed for the current day: " + dayOfWeek + "\n";
							
							continue;
						}
					}
					
					Iterator<Staff> staffIterator = staffList.iterator();
					Staff staff = null;
					
					int index = -1;
					staff: while (staffIterator.hasNext()) {
					
						++index;
						staff = staffIterator.next();
						if (isBusyStaff[index]) {
							continue;
						}
						
						log += "\n\n+++++++++++++++++++++++" + staff + "++++++++++++++++++++++++++++\n";
						
						char shiftForTheDay = '0';
						
						
						Set<StaffShift> staffShieftSet = staff.getStaffShiftSet();
						for (StaffShift staffShift: staffShieftSet) {
							
							effectDate = staffShift.getEffectiveDate();
							expireDate = staffShift.getExpireDate();
							
							if (staffShift.checkExpired(currentDate)) {
								
								log += "Staff shieft is out of date. Current date: " + currentDate +
										" shift effective date " + effectDate + " expire " + expireDate + "\n";
								continue;
							}
							
							else {
								ShiftPattern shiftPattern = staffShift.getShiftPattern();
								
								int days = Days.daysBetween(effectDate, currentDate).getDays();
								String pattern = shiftPattern.getName();
								int numOfDay = days % pattern.length();
								
								shiftForTheDay = pattern.charAt(numOfDay);
								if (shiftForTheDay == 'O') {
									
									log += "Staff has day off in the date: " + currentDate + "\n";
									
									sch = new Schedule(null, staff, currentDate, dayOffShift, "Draft", false);
									schedule.add(sch);
									isBusyStaff[index] = true;
									
									continue staff;
								}
								
								break;
							}
						}
						
						Set<StaffLeave> staffLeaveSet = staff.getStaffLeaveSet();
						
						for (StaffLeave staffLeave: staffLeaveSet) {
							
							//if (sameDay) {
							if (currentDate.equals(staffLeave.getDate())) {
							
								log += "Staff has annual leave today\n";
								
								if (staffLeave.getStatus().equalsIgnoreCase("approved")) {
									
									log += "leave is approved\n";
									sch = new Schedule(null, staff, currentDate, leaveShift, "Draft", false);
									schedule.add(sch);
									isBusyStaff[index] = true;
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
						
							rule = ruleIterator.next();
							
							SetupOption option = rule.getSetupOption();
							Reference reference = rule.getReference();
							
							Criteria criteria = rule.getCriteria();
							String staffAttributeName = null;
							String taskAttributeName = null;
							synchronized(this.getClass()) {
								while (staffAttributeName == null || taskAttributeName == null) {
									
									staffAttributeName = option.getAttributeName();
									taskAttributeName = reference.getAttributeName();
									System.out.println(staffAttributeName + ", " + taskAttributeName);
								}
							}
							log += "Rule: " + staffAttributeName + " " + criteria + " " + taskAttributeName + "\n";
							
							//get attribute with given name
							Class<?> staffClass = staff.getClass();
							Class<?> taskClass = taskProfile.getClass();
							
							Field staffField;
							Field taskField;
							try {
								Object staffObject = null;
								Object taskObject = null;
								synchronized(this.getClass()) {
									staffField = staffClass.getDeclaredField(staffAttributeName);
									taskField = taskClass.getDeclaredField(taskAttributeName);
									
									staffField.setAccessible(true);
									taskField.setAccessible(true);
									
									staffObject = staffField.get(staff);
									taskObject = taskField.get(taskProfile);
								}
							
								log += "STAFF OBJECT: " + staffObject + ", TASK OBJECT: " + taskObject + "\n";
								
								//skip this rule if task object doesn't have it specified
								if (taskObject == null) {
									continue;
								}
								
								//Shift attributes
								else if (taskObject instanceof Shift) {
									log += "staff and task type is Shift\n";
										
									String taskShift = taskProfile.getShift().getShiftLetter();
									char taskShiftCharacter = taskShift.charAt(0);
									log += "staff shift: " + shiftForTheDay + "\n";
									if (!criteria.equals(Criteria.EQUAL)) {
										throw new RosterEngineException("Invalid rule: only EQUALS applies to the Shift rule.\n"
												+ "Currently selected rule: " + criteria.toString());
									}
									else if (taskShiftCharacter == shiftForTheDay) {
										log += "Shift rule approved!\n";
						
										continue;
									}
									else {
										log += "Shift rule not approved\n";
						
										continue staff;
									}
										
							
								}
								//Location attribute. 
								else if (taskObject instanceof Location) {
									
									log += "Location rule\n";
									
									if (!(staffObject instanceof Collection)) {
										throw new RosterEngineException("Task attribute type is Location. Staff should be of type Collection<StaffLocation>\n"
												+ "Current type: " + staffObject.getClass().getSimpleName());
									}
									else {
										
										String setupOption = option.getSetupOption();
										
										List<StaffLocation> staffLocations = new ArrayList<StaffLocation>();
										
										//Assigned or restricted locations
										@SuppressWarnings("rawtypes")
										Collection staffColl = (Collection)staffObject;
										for (Object obj: staffColl) {
											if (!(obj instanceof StaffLocation)) {
												throw new RosterEngineException("Task type is Location. Each attribute of Staff object should be of type StaffLocation\n"
														+ "Current type: " + obj.getClass().getSimpleName());
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
									
									continue;
								}
								
								if (softRuleViolated.size() < numStaffLeft && rule.getType().equals(RuleType.Soft)) {
									log += "Staff violates soft rule\n";
									
									violatesSoftRule = true;
									continue;
								}
								//HARD rule
								else {
									//check next staff
									log += "Staff doesn't satisfy the rule\n";
									
									continue staff;
								}
								
							
							} catch (NoSuchFieldException | SecurityException e) {
								
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								
								e.printStackTrace();
							} catch (RosterEngineException e) {
								System.err.println("EXCETION OCCURED: " + e.getMessage());
								listener.rosterExceptionOccured(e);
								
							} 
							
						}//rule list end
						
						//soft rule violated but candidate founded
						if (violatesSoftRule && numStaffLeft > softRuleViolated.size()) {
							//add candidate as soft violation variant for this task and day
							
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
							
							log += "Schedule  added:\n";
							log += "Task: " + taskProfile + "\n";
							log += "Staff: " + staff + "\n";
							log += "Day: " + currentDate + "\n";
							log += "Shift: " + taskProfile.getShift() + "\n";
							
							sch = new Schedule(taskProfile, staff, currentDate, taskProfile.getShift(), "Draft", false);
							schedule.add(sch);
							
							isBusyStaff[index] = true;
							
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
							
							log += "Violated Schedule added:\n";
							log += "Task: " + taskProfile + "\n";
							log += "Staff: " + violatedSch.getStaff() + "\n";
							log += "Day: " + currentDate + "\n";
							log += "Shift: " + taskProfile.getShift() + "\n";
							
							schedule.add(violatedSch);
							isBusyStaff[violatedStaffIndexes.get(i)] = true;
							
							++i;
							
						}
						
					}
					else {
						
						log += "There are no candidates for current task for the day\n";
						log += "Task: " + taskProfile + "\n";
						log += "Day: " + currentDate + "\n";
					}
					
					
				}//task list end
				error = false;
			}
			catch (RuntimeException e) {
				System.out.println("RUNTIME EXCEPTION: " + e.getLocalizedMessage());
				error = true;
				schedule = new ArrayList<Schedule>();
				
			}
		}//while end
		
	}
	
	//PRIVATE SUPPORT METHODS


	//return positive int if condition is sutisfied, 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean processCriteria(Criteria criteria, Object taskObject, Object staffObject, LocalDate currentDate) throws RosterEngineException {
		
		Collection<Object> staffNotExpired = null;
		Expiring expStaffObj = null;
		boolean expiring = false;
		// Expiring case
		if (staffObject instanceof Collection) {
			
			staffNotExpired = new HashSet<Object>();
			
			//remove expired items
			
			for (Object staffObj: (Collection<?>)staffObject) {
				if (expiring == true || staffObj instanceof Expiring) {
					expiring = true;
					expStaffObj = (Expiring)staffObj;
					if (!expStaffObj.checkExpired(currentDate)) {
						log += "Not expired item added: " + expStaffObj.getValueObject() + "\n";
			
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
			
			if (taskObject instanceof CustomEquality && staffObject instanceof CustomEquality) {
				CustomEquality taskAttr = (CustomEquality)taskObject;
				CustomEquality staffAttr = (CustomEquality)staffObject;
				log += "taskAttr: " + taskAttr + ", staffAttr: " + staffAttr + "\n";
				if (staffAttr.isEqual(taskAttr)) {
			
					log += "Staff value: " + staffObject + " is equal to Task value: " + taskObject + "(CUSTOM EQUALITY)\n";
			
					return true;
				}
			}
			else if (taskObject instanceof Attribute && staffObject instanceof Attribute) {
				Attribute taskAttr = (Attribute)taskObject;
				Attribute staffAttr = (Attribute)staffObject;
				if (staffAttr.getValue().equals(taskAttr.getValue())) {
			
					log += "Staff value: " + staffObject + " is equal to Task value: " + taskObject + "\n";
			
					return true;
				}
			}
			else if (staffObject instanceof Comparable && taskObject instanceof Comparable) {
				
				Comparable compStaff = (Comparable) staffObject;
				Comparable compTask = (Comparable) taskObject;
				
				if (compStaff.compareTo(compTask) == 0) {
					log += "Staff value: " + staffObject + " is equal to Task value: " + taskObject + "\n";
			
					return true;
				}
			}
			return false;
			
		case NOT_EQUAL:
			
			log += "taskObject: " + taskObject + ", staffObject: " + staffObject + "\n";
			
			
			if (taskObject instanceof CustomEquality && staffObject instanceof CustomEquality) {
				CustomEquality taskAttr = (CustomEquality)taskObject;
				CustomEquality staffAttr = (CustomEquality)staffObject;
				log += "taskAttr: " + taskAttr + ", staffAttr: " + staffAttr + "\n";
				if (!staffAttr.isEqual(taskAttr)) {
			
					log += "Staff value: " + staffObject + " is not equal to Task value: " + taskObject + "(CUSTOM EQUALITY)\n";
			
					return true;
				}
			}
			
			else if (taskObject instanceof Attribute && staffObject instanceof Attribute) {
				Attribute taskAttr = (Attribute)taskObject;
				Attribute staffAttr = (Attribute)staffObject;
				if (!staffAttr.getValue().equals(taskAttr.getValue())) {
					log += "Staff value: " + staffObject + " is not equal to Task value: " + taskObject + "\n";
			
					return true;
				}
				return false;
			}
			else if (staffObject instanceof Comparable && taskObject instanceof Comparable) {
				
				Comparable compStaff = (Comparable) staffObject;
				Comparable compTask = (Comparable) taskObject;
				
				if (compStaff.compareTo(compTask) != 0) {
					log += "Staff value: " + staffObject + " is equal to Task value: " + taskObject + "\n";
			
					return true;
				}
			}
			else {
				return true;
			}
		case CONTAINS:
			if (!(staffObject instanceof Collection || taskObject instanceof Collection)) {
				throw new RosterEngineException("CONTAINS rule is applicable only for Collection of attributes.\n "
						+ "Staff object type: " + staffObject.getClass().getSimpleName() + ", Task object type: " + taskObject.getClass().getSimpleName()
						+ "\nPlease choose appropriate criteria for the attributes in the rules configuration page");
			}
			Collection<?> staffCollection = (Collection)staffObject;
			if (taskObject instanceof Collection) {
				Collection<?> taskCollection = (Collection)taskObject;
				if (staffCollection.containsAll(taskCollection)) {
					log += "Staff collection contains all objects from Task collection\n";
			
					return true;
				}
			}
			else {
				if (staffCollection.contains(taskObject)) {
					log += "Staff collection contains all objects from Task collection\n";
			
					return true;
				}
			}
		
			return false;
		case IN_BETWEEN:
			if (taskObject instanceof Range) {
				if (!(staffObject instanceof Comparable<?>)) {
					throw new RosterEngineException("Task rule type is Range. Expected type of task is Comparable.\n Current type: " 
							+ staffObject.getClass().getSimpleName()
							+ "\nPlease choose appropriate criteria for the attributes in the rules configuration page");
				}
				else {
					Range taskRange = (Range)taskObject;
					
					Comparable<Number> staffNumber = (Comparable<Number>) staffObject;
					if (taskRange.checkInBetween(staffNumber)) {
						log += "Staff attribute is in between task range\n";
		
						return true;
					}
				}
			}
			else {
				throw new RosterEngineException("IN_BETWEEN rule is applicable only for Task type: Range, Staff type: Comparable type.\n"
						+ " Current Staff type: " + staffObject.getClass().getSimpleName() + ", Task type: " + taskObject.getClass().getSimpleName()
						+ "\nPlease choose appropriate criteria for the attributes in the rules configurations");
			}
			return false;
		case ATLEAST:
			if (!(staffObject instanceof Comparable && taskObject instanceof Comparable)) {
				throw new RosterEngineException("ATLEAST rule is applicable only for Comparable types.\n "
						+ "Current Staff type: " + staffObject.getClass().getSimpleName() + ", Task type: " + taskObject.getClass().getSimpleName()
						+ "\nPlease choose appropriate criteria for the attributes in the rules configurations");
			}
			else {
				Comparable compStaff = (Comparable) staffObject;
				Comparable compTask = (Comparable) taskObject;
				
				if (compStaff.compareTo(compTask) >= 0) {
					log += "Staff attribute is bigger than task attribute\n";
		
					return true;
				}
			}
			return false;
		case ATMOST:
			if (!(staffObject instanceof Comparable && taskObject instanceof Comparable)) {
				throw new RosterEngineException("ATMOST rule is applicable only for Comparable types.\n "
						+ "Current Staff type: " + staffObject.getClass().getSimpleName() + ", Task type: " + taskObject.getClass().getSimpleName()
						+ "\nPlease choose appropriate criteria for the attributes in the rules configurations");
			}
			else {
				Comparable compStaff = (Comparable) staffObject;
				Comparable compTask = (Comparable) taskObject;
				
				if (compStaff.compareTo(compTask) <= 0) {
					log += "Staff attribute is smaller than task attribute\n";
		
					return true;
				}
			}
			return false;
		default:
			return false;
		}
	}
	
	
	
}
