package com.ascendaz.roster.repository;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ascendaz.roster.model.RosterUser;

@Repository("userRepository")
public class UserRepository {
	
	/*@PersistenceContext
	private EntityManager em;*/
	@Autowired
	private SessionFactory sessionFactory;
	
	public RosterUser getUserByUsername(String username) {
	/*	TypedQuery<RosterUser> query = em.createNamedQuery(RosterUser.GET_USER_BY_USERNAME, RosterUser.class);
		query.setParameter("username", username);
		List<RosterUser> users = query.getResultList();
		if (users.size() == 0) return null;
		RosterUser userFromDB = users.get(0);
		
		return userFromDB;*/
		
		String hqlQuery = "FROM RosterUser "
						+ "WHERE username = :username";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("username", username);
		RosterUser user = (RosterUser)query.uniqueResult();
		return user;
		
	}
	/*public List<RosterUser> getAllUsers() {
		TypedQuery<RosterUser> query = em.createNamedQuery(RosterUser.GET_ALL_USERS, RosterUser.class);
		List<RosterUser> users = query.getResultList();
		System.out.println("USERS:");
		for (RosterUser user: users) {
			System.out.println(user.getUsername() + "  " + user.getPassword());
		}
		return users;
	}*/
	
}
