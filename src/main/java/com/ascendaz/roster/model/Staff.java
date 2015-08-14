package com.ascendaz.roster.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import com.ascendaz.roster.model.attributes.Designation;
import com.ascendaz.roster.model.attributes.Gender;
import com.ascendaz.roster.model.attributes.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "staff")
public class Staff implements Comparable<Staff>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8466938937045060086L;

	public static final Comparator<Staff> SALARY_COMPARATOR = new SalaryComparator();

	@Id
	@Column(name="STAFF_ID", nullable=false)
	private int id;
	
	@Column(name="NAME", nullable=false, length=50)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GENDER_ID", nullable = false)
	@JsonIgnore
	private Gender gender;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DESIGNATION_ID", nullable = false)
	@JsonIgnore
	private Designation designation;
	
	@Column(name="SALARY", nullable=false)
	private int salary;
	
	@Column(name="AGE", nullable=false)
	private Integer age;
	
	@Column(name="DATE_OF_BIRTH", nullable=true)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate dateOfBirth;
	
	@Column(name="JOIN_DATE", nullable=false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate joinDate;
	
	@Column(name="RESIGN_DATE", nullable=false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate resignDate;
	
	
	@JoinTable(name = "staff_skill", 
            joinColumns = { 
                   @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID")
            }, 
            inverseJoinColumns = { 
                   @JoinColumn(name = "SKILL_ID", referencedColumnName = "SKILL_ID")
            }
     )
    @ManyToMany
    @JsonIgnore
    private Set<Skill> skillSet = new HashSet<Skill>();;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "staff")
	@JsonManagedReference
	private Set<StaffLocation> staffLocationSet = new HashSet<StaffLocation>();

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "staff")
	@JsonIgnore
	private Set<StaffShift> staffShiftSet = new HashSet<StaffShift>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "staff")
	@JsonIgnore
	private Set<StaffTraining> staffTrainingSet = new HashSet<StaffTraining>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "staff")
	@JsonIgnore
	private Set<StaffLeave> staffLeaveSet = new HashSet<StaffLeave>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "staff")
	@JsonIgnore
	private Set<Schedule> scheduleSet = new HashSet<Schedule>();
	
	@Transient
	private boolean isBusy;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<StaffLocation> getStaffLocationSet() {
		return staffLocationSet;
	}

	public void setStaffLocationSet(Set<StaffLocation> staffLocationSet) {
		this.staffLocationSet = staffLocationSet;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Set<Skill> getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(Set<Skill> skillSet) {
		this.skillSet = skillSet;
	}

	public Set<StaffShift> getStaffShiftSet() {
		return staffShiftSet;
	}

	public void setStaffShiftSet(Set<StaffShift> staffShiftSet) {
		this.staffShiftSet = staffShiftSet;
	}

	public Set<StaffTraining> getStaffTrainingSet() {
		return staffTrainingSet;
	}

	public void setStaffTrainingSet(Set<StaffTraining> staffTrainingSet) {
		this.staffTrainingSet = staffTrainingSet;
	}

	public Set<StaffLeave> getStaffLeaveSet() {
		return staffLeaveSet;
	}

	public void setStaffLeaveSet(Set<StaffLeave> staffLeaveSet) {
		this.staffLeaveSet = staffLeaveSet;
	}

	public Set<Schedule> getScheduleSet() {
		return scheduleSet;
	}

	public void setScheduleSet(Set<Schedule> scheduleSet) {
		this.scheduleSet = scheduleSet;
	}

	
	public boolean isBusy() {
		return isBusy;
	}
	
	@Override
	public int compareTo(Staff staff) {
		
		return this.getName().compareTo(staff.getName());

	}


	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public LocalDate getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}

	public LocalDate getResignDate() {
		return resignDate;
	}

	public void setResignDate(LocalDate resignDate) {
		this.resignDate = resignDate;
	}

	@Override
	public String toString() {
		return "Staff: id=" + id + ", name=" + name + ", gender=" + gender + ", designation=" + designation
				+ ", salary=" + salary + ", age=" + age;
	}
	
	static class SalaryComparator implements Comparator<Staff> {
        public int compare(Staff s1, Staff s2) {
        	return s1.salary - s2.salary;
        }
    }
}
