package com.ascendaz.roster.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import com.ascendaz.roster.model.attributes.Location;
import com.ascendaz.roster.model.attributes.interfaces.Expiring;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="staff_location", indexes = {
        @Index(columnList = "STAFF_ID", name = "staff_index"),
        @Index(columnList = "LOCATION_ID", name = "location_index")
})
        
public class StaffLocation implements Expiring, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2579199913620718434L;

	@Id
	@Column(name="STAFF_LOCATION_ID", nullable=false)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAFF_ID", nullable = false)
	@JsonBackReference
	private Staff staff;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCATION_ID", nullable = false)
	@JsonManagedReference
	private Location location;
	
	@Column(name = "TYPE", nullable = false, length = 10)
	private String type;
	
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		return this.location;
	}
	
	
	
}
