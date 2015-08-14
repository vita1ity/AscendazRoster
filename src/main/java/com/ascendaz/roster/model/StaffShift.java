package com.ascendaz.roster.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import com.ascendaz.roster.model.attributes.interfaces.Expiring;

@Entity
@Table(name = "staff_shift")
public class StaffShift implements Expiring, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4034753572075408886L;

	@Id
	@Column(name = "STAFF_SHIFT_ID", nullable = false)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAFF_ID", nullable = false)
	private Staff staff;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHIFT_PATTERN_ID", nullable = false)
	private ShiftPattern shiftPattern;
	
	@Column(name = "EFFECTIVE_DATE", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate effectiveDate;
	
	@Column(name = "EXPIRE_DATE", nullable = true)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate expireDate;

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

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public LocalDate getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}
	
	@Override
	public boolean checkExpired(LocalDate currentDate) {
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
