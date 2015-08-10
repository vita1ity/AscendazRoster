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
import com.ascendaz.roster.model.attributes.Training;

@Entity
@Table(name = "staff_training")
public class StaffTraining implements Expiring, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6641989028579526049L;

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "STAFF_TRAINING_ID")
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAFF_ID", nullable = false)
	private Staff staff;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRAINING_ID", nullable = false)
	private Training training;
	
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

	public Training getTraining() {
		return training;
	}

	public void setTraining(Training training) {
		this.training = training;
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
		
		return this.training;
	}
	
	
}
