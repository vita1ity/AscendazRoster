package com.ascendaz.roster.repository;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ascendaz.roster.model.RosterUser;

@Repository("userRepository")
public class UserRepository {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public RosterUser getUserByUsername(String username) {
		
		String hqlQuery = "FROM RosterUser "
						+ "WHERE username = :username";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("username", username);
		RosterUser user = (RosterUser)query.uniqueResult();
		return user;
		
	}
		
}
