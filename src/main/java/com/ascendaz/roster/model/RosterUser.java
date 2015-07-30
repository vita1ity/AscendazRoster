package com.ascendaz.roster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "roster_user")
@NamedQueries({
	@NamedQuery(name=RosterUser.GET_USER_BY_USERNAME, query="SELECT user1 "
												+ "FROM RosterUser user1 "
												+ "WHERE user1.username = :username"),
	@NamedQuery(name=RosterUser.GET_ALL_USERS, query="SELECT user1 "
											+ "FROM RosterUser user1"),
})
public class RosterUser {
	
	public static final String GET_USER_BY_USERNAME = "getUserByUsername";
	public static final String GET_ALL_USERS = "getAllUsers";

	@Id
	@Column(name="USER_ID", nullable=false)
	private int userId;
	
	@Column(name="USERNAME", nullable=false)
	private String username;
	
	@Column(name="PASSWORD", nullable=false)
	private String password;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}