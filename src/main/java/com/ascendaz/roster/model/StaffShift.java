package com.ascendaz.roster.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ascendaz.roster.model.attributes.Expiring;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "staff_shift")
public class StaffShift implements Expiring, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4034753572075408886L;

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "STAFF_SHIFT_ID", nullable = false)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAFF_ID", nullable = false)
	private Staff staff;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHIFT_PATTERN_ID", nullable = false)
	private ShiftPattern shiftPattern;
	
	@Column(name = "EFFECTIVE_DATE", nullable = false)
	private Date effectiveDate;
	
	@Column(name = "EXPIRE_DATE", nullable = true)
	private Date expireDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public ShiftPattern getShiftPattern() {
		return shiftPattern;
	}

	public void setShiftPattern(ShiftPattern shiftPattern) {
		this.shiftPattern = shiftPattern;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	@Override
	public boolean checkExpired(Date currentDate) {
		if (currentDate.compareTo(this.effectiveDate) >= 0 && currentDate.compareTo(this.expireDate) <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public Object getValueObject() {
		
		return this.shiftPattern;
	}
	
	
}
