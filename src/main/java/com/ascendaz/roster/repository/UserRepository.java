package com.ascendaz.roster.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ascendaz.roster.model.RosterUser;

@Repository("userRepository")
public class UserRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public RosterUser getUserByUsername(String username) {
		TypedQuery<RosterUser> query = em.createNamedQuery(RosterUser.GET_USER_BY_USERNAME, RosterUser.class);
		query.setParameter("username", username);
		List<RosterUser> users = query.getResultList();
		if (users.size() == 0) return null;
		RosterUser userFromDB = users.get(0);
		
		return userFromDB;
	}
	public List<RosterUser> getAllUsers() {
		TypedQuery<RosterUser> query = em.createNamedQuery(RosterUser.GET_ALL_USERS, RosterUser.class);
		List<RosterUser> users = query.getResultList();
		System.out.println("USERS:");
		for (RosterUser user: users) {
			System.out.println(user.getUsername() + "  " + user.getPassword());
		}
		return users;
	}
	
}
