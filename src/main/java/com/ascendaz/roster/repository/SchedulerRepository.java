package com.ascendaz.roster.repository;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.attributes.Shift;
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

	public void saveSchedule(List<Schedule> schedule) {
		
		Session session = sessionFactory.getCurrentSession();
		for (Schedule sch: schedule) {
			Transaction tx = session.beginTransaction();
			try {
				
				session.save(sch);
				
			}
			catch (HibernateException e) {
				e.printStackTrace();
				session.getTransaction().rollback();
			}
			if (!tx.wasCommitted()) {
				tx.commit();
			}
		}

		
	}

	public Shift getShiftByShiftLetter(String shiftLetter) {
		String hqlQuery = "FROM Shift "
				+ "WHERE shiftLetter = :shiftLetter";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("shiftLetter", shiftLetter);
		Shift shift = (Shift)query.uniqueResult();
		return shift;
		
	}
	
	
}	
