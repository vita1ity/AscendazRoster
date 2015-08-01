package com.ascendaz.roster.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "leave")
public class Leave {
	
	@Id
	@GeneratedValue
	@Column(name="LEAVE_ID", nullable=false)
	private int id;
	
	@Column(name="TYPE", nullable=false, length=2)
	private String type;
	
	@Column(name="SEQUENCE", nullable=false)
	private int sequence;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "leave")
	private Set<StaffLeave> staffLeaveSet = new HashSet<StaffLeave>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Set<StaffLeave> getStaffLeaveSet() {
		return staffLeaveSet;
	}

	public void setStaffLeaveSet(Set<StaffLeave> staffLeaveSet) {
		this.staffLeaveSet = staffLeaveSet;
	}
	
	
}
