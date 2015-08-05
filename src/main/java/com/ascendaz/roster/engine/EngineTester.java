package com.ascendaz.roster.engine;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.attributes.AgeRange;
import com.ascendaz.roster.model.attributes.Skill;

public class EngineTester {
	
	public static void main(String[] args) 
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		/*Staff staff = new Staff();
		staff.setAge(40);
		Gender gender = new Gender();
		gender.setType("M");
		staff.setGender(gender);
		Class<?> c = staff.getClass();

		Field f = c.getDeclaredField("gender");
		f.setAccessible(true);

		Object valueOfGender = f.get(staff);
		
		if (valueOfGender instanceof Attribute) {
			Attribute attrGender = (Attribute)valueOfGender;
			
			Object value = attrGender.getValue();
			
			if (value.equals("M")) {
				System.out.println("EQUAL");
			}
			//compGender.getValue();
		}
		
		System.out.println(valueOfGender);*/
		
		
		//list of values
		Set<Skill> skillSet = new HashSet<Skill>();
		Skill skill1 = new Skill();
		skill1.setSkill("MVC");
		
		Skill skill2 = new Skill();
		skill2.setSkill("AJAX");
		
		Skill skill3 = new Skill();
		skill3.setSkill("J2EE");
		skillSet.add(skill1);
		skillSet.add(skill2);
		skillSet.add(skill3);
		
		Staff staff = new Staff();
		staff.setSkillSet(skillSet);
		
		Skill comparedSkill = new Skill();
		comparedSkill.setSkill("AJAX");
	
		
		Set<Skill> comparedSkills = new HashSet<Skill>();
		comparedSkills.add(skill1);
		comparedSkills.add(skill3);
		comparedSkills.add(comparedSkill);
		
		Class<?> staffClass = staff.getClass();
		Field staffField = staffClass.getDeclaredField("skillSet");
		staffField.setAccessible(true);
		
		Object valueOfSkills = staffField.get(staff);
		if (valueOfSkills instanceof Collection) {
			System.out.println("Collection");
			Collection<?> colSkills = (Collection<?>) valueOfSkills;
			
			System.out.println(colSkills.containsAll(comparedSkills));
			System.out.println(colSkills.equals(comparedSkills));
			
			
			for (Object skill: colSkills) {
				
				
				if (skill instanceof Comparable) {
					
					Comparable compSkill = (Comparable) skill;
					
				}
			}
		}
		AgeRange ageRange = new AgeRange();
		ageRange.setStartAge(5);
		ageRange.setEndAge(10);
		
		Object number = new Integer(6);
		System.out.println("In between: " + ageRange.checkInBetween((Comparable<Number>)number));
		
		
	}
	
}
