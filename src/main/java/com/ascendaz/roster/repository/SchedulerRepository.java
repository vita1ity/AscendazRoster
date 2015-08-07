package com.ascendaz.roster.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.config.Rule;

@Repository("schedulerRepository")
public class SchedulerRepository {
	/*@PersistenceContext
	private EntityManager em;*/
	
	@Autowired
	private SessionFactory sessionFactory;

	public List<TaskProfile> getTasks() {
		/*TypedQuery<TaskProfile> query = em.createNamedQuery(TaskProfile.GET_ALL_TASKS, TaskProfile.class);
		List<TaskProfile> tasks = query.getResultList();
		System.out.println("TASKS:");
		for (TaskProfile task: tasks) {
			System.out.println("Task: " + task);
		}
		return tasks;
		*/
		
		String hqlQuery = "FROM TaskProfile";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		List<TaskProfile> tasks = query.list();
		return tasks;
	}

	public List<Staff> getStaff() {
		/*TypedQuery<Staff> query = em.createNamedQuery(Staff.GET_ALL_STAFF, Staff.class);
		List<Staff> staff = query.getResultList();
		System.out.println("STAFF:");
		for (Staff s: staff) {
			System.out.println("Staff: " + s);
		}
		return staff;*/
		String hqlQuery = "FROM Staff";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		List<Staff> staff = query.list();
		return staff;
	}

	public List<Rule> getSelectedRules() {
		/*TypedQuery<Rule> query = em.createNamedQuery(Rule.GET_SELECTED_RULES, Rule.class);
		List<Rule> rules = query.getResultList();
		System.out.println("RULES:");
		for (Rule r: rules) {
			System.out.println("Rule: " + r);
		}
		return rules;*/
		
		String hqlQuery = "FROM Rule "
						+ "WHERE isSelected = true";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		List<Rule> rules = query.list();
		return rules;
	
	}
	
	
}	
