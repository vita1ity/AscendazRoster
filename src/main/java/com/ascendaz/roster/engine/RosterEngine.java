package com.ascendaz.roster.engine;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.ascendaz.roster.exception.RosterEngineException;
import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.model.ShiftPattern;
import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.StaffLeave;
import com.ascendaz.roster.model.StaffShift;
import com.ascendaz.roster.model.Task;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.attributes.DayOfWeek;
import com.ascendaz.roster.model.attributes.Expiring;
import com.ascendaz.roster.model.attributes.Range;
import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.config.Criteria;
import com.ascendaz.roster.model.config.Reference;
import com.ascendaz.roster.model.config.Rule;
import com.ascendaz.roster.model.config.RuleType;
import com.ascendaz.roster.model.config.SetupOption;

public class RosterEngine {
	
	private List<TaskProfile> taskList;
	private List<Staff> staffList;
	private List<Rule> ruleList;
	
	public RosterEngine(List<TaskProfile> taskList, List<Staff> staffList, List<Rule> ruleList) {
		super();
		this.taskList = taskList;
		this.staffList = staffList;
		this.ruleList = ruleList;
	}

	//TODO
	//@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Schedule> processRules(Date startDate, Date endDate) throws RosterEngineException {
		
		List<Schedule> schedule = new ArrayList<Schedule>();
		
		Schedule sch = null;
		List<Schedule> softRuleViolated = null;
		
		int numStaffLeft = 0;
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		//sort staff by salary
		Collections.sort(staffList);
		//sort rules by priority
		Collections.sort(ruleList);
		
		Date currentDate = startDate;
		while (!currentDate.equals(endDate)) {
			
			System.out.println("Current Date: " + df.format(currentDate));
			List<Staff> candidatesForCurrentDay = new ArrayList<Staff>(staffList);
			
			Task task = null;
			nextTask: for (TaskProfile taskProfile: new ArrayList<TaskProfile>(taskList)) {
				
				
				softRuleViolated = new ArrayList<Schedule>();
				
				task = taskProfile.getTask();
				int numOfStaffNeeded = task.getHeadcount();
				numStaffLeft = numOfStaffNeeded;
				
				//check if Task is valid for this date
				Date taskStartDate = task.getStartDate();
				Date taskEndDate = task.getEndDate();
				if (taskStartDate.compareTo(currentDate) > 0 || taskEndDate.compareTo(currentDate) < 0) {
					
					System.out.println("Task does not need to be peformed for current date. Current date: " + df.format(currentDate) +
							" task dates: from " + df.format(taskStartDate) + " to " + df.format(taskEndDate));
					
					taskList.remove(task);
					continue;
				}
				List<DayOfWeek> daysOfWeek= task.getDayOfWeekSet();
				
				
				//check if task should be performed in current day of week
				boolean isPerformedForCurrentDayOfWeek = false;
				//0 is equal to all days of week
				if (daysOfWeek.size() == 1 && daysOfWeek.get(0).getNumber() == 0) {
					
					System.out.println("Day should be performed for the whole week");
							
					isPerformedForCurrentDayOfWeek = true;
				}
				if (!isPerformedForCurrentDayOfWeek) {
					
					cal.setTime(currentDate);
					int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
					for (DayOfWeek day: daysOfWeek) {
						if (day.getNumber() == dayOfWeek) {
							System.out.println("Day should be performed for the day: " + dayOfWeek);
							isPerformedForCurrentDayOfWeek = true;
							break;
						}
					}
					if (!isPerformedForCurrentDayOfWeek) {
						taskList.remove(task);
						continue;
					}
				}
				
				
				
				Date effectDate = null;
				Date expireDate = null;
				LocalDate effectLocalDate = null;
				LocalDate currentLocalDate = new LocalDate(currentDate);
				
				staff: for (Staff staff: new ArrayList<Staff>(candidatesForCurrentDay)) {
					
					char shiftForTheDay = '0';
					
					
					//TODO change using Expiring interface
					Set<StaffShift> staffShieftSet = staff.getStaffShiftSet();
					for (StaffShift staffShift: staffShieftSet) {
						if (staffShift.checkExpired(currentDate)) {
							System.out.println("Staff shieft is out of date. Current date: " + df.format(currentDate) +
									" shieft effective date " + df.format(effectDate) + " expire " + df.format(expireDate));
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
							effectLocalDate = new LocalDate(effectDate);
							
							int days = Days.daysBetween(effectLocalDate, currentLocalDate).getDays();
							String pattern = shiftPattern.getName();
							int numOfDay = days % pattern.length();
							
							shiftForTheDay = pattern.charAt(numOfDay);
							if (pattern.charAt(numOfDay) == 'O') {
								
								System.out.println("Staff has day off in the date: " + df.format(currentDate));
								Shift shift = new Shift();
								shift.setShift("OFF");
								shift.setShiftLetter("O");
								
								sch = new Schedule(null, staff, shift, false);
								schedule.add(sch);
								candidatesForCurrentDay.remove(staff);
								continue staff;
							}
							
							
							break;
						}
					}
					
					Set<StaffLeave> staffLeaveSet = staff.getStaffLeaveSet();
					
					for (StaffLeave staffLeave: staffLeaveSet) {
						if (staffLeave.getDate().equals(currentDate)) {
							System.out.println("Staff has annual leave today");
							
							if (staffLeave.getStatus().equalsIgnoreCase("approved")) {
								
								System.out.println("leave is approved");
								
								Shift shift = new Shift();
								shift.setShift("Annual Leave");
								shift.setShiftLetter("L");
								sch = new Schedule(null, staff, shift, false);
								schedule.add(sch);
								candidatesForCurrentDay.remove(staff);
								continue staff;
							}
							break;
						}
					}
					
					
					//RULES CHECKING
					boolean violatesSoftRule = false;
		
					for (Rule rule: ruleList) {
						
						SetupOption option = rule.getSetupOption();
						Reference reference = rule.getReference();
						Criteria criteria = rule.getCriteria();
						
						String attributeName = option.getAttributeName();
						String taskAttributeName = reference.getAttributeName();
						
						System.out.println("Rule with staff attribute: " + attributeName + " task attribute: " + taskAttributeName);
						
						//get attribute with given name
						Class<?> staffClass = staff.getClass();
						Class<?> taskClass = taskProfile.getClass();
						
						Field staffField;
						Field taskField;
						try {
							staffField = staffClass.getDeclaredField(attributeName);
							taskField = taskClass.getDeclaredField(taskAttributeName);
							
							staffField.setAccessible(true);
							taskField.setAccessible(true);
							
							Object staffObject = staffField.get(staff);
							Object taskObject = taskField.get(taskProfile);
							
							//Shift attributes
							if (staffObject instanceof Shift) {
								if (!(taskObject instanceof Shift)) {
									throw new RosterEngineException("Staff rule type is Shift. Expected type of task is Shift. Current type: " 
											+ taskObject.getClass().getName());
								}
								else {
									String taskShift = taskProfile.getShift().getShiftLetter();
									char taskShiftCharacter = taskShift.charAt(0);
									
									//shift supports only equals criteria
									
									if (!criteria.equals(Criteria.EQUAL)) {
										throw new RosterEngineException("Invalid rule: only EQUALS applies to the Shift rule");
									}
									else if (taskShiftCharacter == shiftForTheDay) {
										System.out.println("Shift rule approved!");
										continue;
									}
									
								}
							}
							
							boolean res = processCriteria(criteria, taskObject, staffObject, currentDate);
							
							if (res) {
								continue;
							}
							
							if (softRuleViolated.size() < numStaffLeft && rule.getType().equals(RuleType.Soft)) {
								violatesSoftRule = true;
								continue;
							}
							//HARD rule
							else {
								//check next staff
								continue staff;
							}
							
							//check if not Expiring TODO
							
						
						} catch (NoSuchFieldException | SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}//rule list end
					
					
					//soft rule violated but candidate founded
					if (violatesSoftRule && numStaffLeft > softRuleViolated.size()) {
						//add candidate as soft violation variant for this task and day
						Schedule violatedSchedule = new Schedule(taskProfile, staff, taskProfile.getShift(), true);
						softRuleViolated.add(violatedSchedule);
					}
					//candidate satisfies all rules 
					else {
						
						System.out.println("Schedule added:");
						System.out.println("Task: " + taskProfile);
						System.out.println("Staff: " + staff);
						System.out.println("Day: " + currentDate);
						System.out.println("Shift: " + taskProfile.getShift());
						
						sch = new Schedule(taskProfile, staff, taskProfile.getShift(), false);
						schedule.add(sch);
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
					for (Schedule violatedSch: softRuleViolated) {
						System.out.println("Violated Schedule added:");
						System.out.println("Task: " + taskProfile);
						System.out.println("Staff: " + violatedSch.getStaff());
						System.out.println("Day: " + currentDate);
						System.out.println("Shift: " + taskProfile.getShift());
						
						schedule.add(violatedSch);
					}
					
				}
				else {
					System.out.println("There are no candidates for current task for the day");
					System.out.println("Task: " + taskProfile);
					System.out.println("Day: " + currentDate);
				}
				
				
			}//task list end
			
			
			
			//get next day
			Calendar c = Calendar.getInstance(); 
			c.setTime(currentDate); 
			c.add(Calendar.DATE, 1);
			currentDate = c.getTime();
		}//days list end
		
		return schedule;
	}

	//PRIVATE SUPPORT METHODS
	
	//return positive int if condition is sutisfied, 
	private boolean processCriteria(Criteria criteria, Object taskObject, Object staffObject, Date currentDate) throws RosterEngineException {
		
		Collection<?> staffCollection = null;
		Collection<?> taskCollection = null;
		Collection<Expiring> staffNotExpired = null;
		boolean expiring = false;
		// Expiring case
		if (staffObject instanceof Collection && taskObject instanceof Collection) {
			staffCollection = (Collection<?>) staffObject;
			taskCollection = (Collection<?>) taskObject;
			
			staffNotExpired = new HashSet<Expiring>();
			
			//remove expired items
			
			for (Object staffObj: staffCollection) {
				if (expiring == true || staffObj instanceof Expiring) {
					expiring = true;
					Expiring expStaffObj = (Expiring)staffObj;
					if (!expStaffObj.checkExpired(currentDate)) {
						staffNotExpired.add(expStaffObj);
					}
					
				}
				else break;
			}
			
		}
		
		switch (criteria) {
		case EQUAL:
			if (expiring) {
				if (taskCollection.equals(staffNotExpired)) {
					System.out.println("(Not expired) Staff value: " + staffObject + " is equal to Task value: " + taskObject);
					return true;
				}
			}
			else if (taskObject.equals(staffObject)) {
				System.out.println("Staff value: " + staffObject + " is equal to Task value: " + taskObject);
				return true;
			}
			return false;
		case NOT_EQUAL:
			if (!taskObject.equals(staffObject)) {
				System.out.println("Staff value: " + staffObject + " is not equal to Task value: " + taskObject);
				return true;
			}
			return false;
		case CONTAINS:
			if (!(staffObject instanceof Collection && taskObject instanceof Collection)) {
				throw new RosterEngineException("CONTAINS rule is applicable only for Collection type. Staff type: " + staffObject.getClass().getName()
						+ ", task type: " + taskObject.getClass().getName());
			}
			else if (expiring) {
				if (staffNotExpired.containsAll(taskCollection)) {
					System.out.println("Staff collection without expired items contains all objects from Task collection");
					return true;
				}
			}
			else {
				if (staffCollection.containsAll(taskCollection)) {
					System.out.println("Staff collection contains all objects from Task collection");
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
				Comparable compTask = (Comparable) staffObject;
				
				if (compStaff.compareTo(compTask) > 0) {
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
				Comparable compTask = (Comparable) staffObject;
				
				if (compStaff.compareTo(compTask) < 0) {
					return true;
				}
			}
			return false;
		default:
			return false;
		}
		
	}
	

	
	
}
