package com.ascendaz.roster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascendaz.roster.model.RosterUser;
import com.ascendaz.roster.repository.UserRepository;

@Service("userService")
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public boolean checkUser(String username, String password) {
		RosterUser userFromDb = userRepository.getUserByUsername(username);
		if (userFromDb == null)  {
			return false;
		}
		if (userFromDb.getPassword().equals(password)) {
			return true;
		}
		return false;
	}
	
	
}
