package com.ascendaz.roster.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roster_user")

public class RosterUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3682158278899144868L;

	@Id
	@Column(name="USER_ID", nullable=false)
	private int userId;
	
	@Column(name="USERNAME", nullable=false)
	private String username;
	
	@Column(name="PASSWORD", nullable=false)
	private String password;

	public RosterUser() {
		super();
	}
	
	public RosterUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

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
