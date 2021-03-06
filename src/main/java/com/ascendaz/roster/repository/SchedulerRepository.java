package com.ascendaz.roster.repository;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ascendaz.roster.model.Schedule;
import com.ascendaz.roster.model.Staff;
import com.ascendaz.roster.model.TaskProfile;
import com.ascendaz.roster.model.attributes.Location;
import com.ascendaz.roster.model.attributes.Shift;
import com.ascendaz.roster.model.config.Rule;
import com.ascendaz.roster.util.Constants;

@Repository("schedulerRepository")
public class SchedulerRepository {
	
	@Autowired
	private SessionFactory sessionFactory;

	public List<TaskProfile> getTasks() {
		
		String hqlQuery = "FROM TaskProfile";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		List<TaskProfile> tasks = query.list();
		return tasks;
	}
	public List<Staff> getActiveStaffForPage(LocalDate startDate, LocalDate endDate, int page) {
		String hqlQuery = "FROM Staff " 
						+ "WHERE joinDate <= :startDate AND resignDate >= :endDate "
						+ "ORDER BY name";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		
		int firstRes = Constants.STAFF_PER_PAGE * (page - 1);
		int lastRes = Constants.STAFF_PER_PAGE;
		query.setFirstResult(firstRes);
		query.setMaxResults(lastRes);
		
		List<Staff> staff = query.list();
		return staff;
		
	}
	
	public List<Staff> getActiveStaff(LocalDate startDate, LocalDate endDate) {
		String hqlQuery = "FROM Staff " 
						+ "WHERE joinDate <= :startDate AND resignDate >= :endDate";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		
		List<Staff> staff = query.list();
		return staff;
		
	}
	
	public List<Staff> getStaff() {
		String hqlQuery = "FROM Staff";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		List<Staff> staff = query.list();
		return staff;
	}

	public List<Rule> getSelectedRules() {
		
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

	public List<Schedule> getScheduleForPeriod(LocalDate startDate, LocalDate endDate, List<Integer> staff) {
		String hqlQuery = "FROM Schedule "
						+ "WHERE (staff.id IN (:staff)) AND (date BETWEEN :startDate AND :endDate)";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameterList("staff", staff);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<Schedule> schedule = query.list();
		
		return schedule;
	}

	public void removeSchedule(LocalDate startDate, LocalDate endDate) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		try {
			String hqlQuery = "DELETE FROM Schedule "
							+ "WHERE date between :start and :end";
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("start", startDate);
			query.setParameter("end", endDate);
			int rows = query.executeUpdate();
			tx.commit();
		}
		catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
	}

	public void setApprovedStatus(LocalDate startDate, LocalDate endDate) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		String status = "Submitted";
		try {
			//System.out.println("Approve status. startDate: " + startDate + ", endDate: " + endDate);
			String hqlQuery = "UPDATE Schedule "
							+ "SET status = :status "
							+ "WHERE date between :start and :end";
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("status", status);
			query.setParameter("start", startDate);
			query.setParameter("end", endDate);
			int rows = query.executeUpdate();
			System.out.println(rows + "rows updated");
			tx.commit();
		}
		catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		}
		
	}

	public List<Location> getAllLocations() {
		String hqlQuery = "FROM Location";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		List<Location> locations = query.list();
		return locations;
	}
	public int getNumberOfStaff(LocalDate startDate, LocalDate endDate) {
		String hqlQuery = "SELECT COUNT(*) FROM Staff " +
						"WHERE joinDate <= :startDate AND resignDate >= :endDate";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		Long count = (Long)query.uniqueResult();
		
		return count.intValue();
	}
	public List<Integer> getStaffForPage(LocalDate startDate, LocalDate endDate, int page) {
		String hqlQuery = "SELECT id FROM Staff "
				+ "WHERE joinDate <= :startDate AND resignDate >= :endDate "
				+ "ORDER BY name";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		int firstRes = Constants.STAFF_PER_PAGE * (page - 1);
		int lastRes = Constants.STAFF_PER_PAGE;
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setFirstResult(firstRes);
		query.setMaxResults(lastRes);
		
		List<Integer> staff = query.list();
		for (Integer id: staff) {
			System.out.println("Staff: " + id);
		}
	
		return staff;
	}

}	
