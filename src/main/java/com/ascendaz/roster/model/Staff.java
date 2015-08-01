package com.ascendaz.roster.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff {
	
	@Id
	@GeneratedValue
	@Column(name="STAFF_ID", nullable=false)
	private int id;
	
	@Column(name="NAME", nullable=false, length=50)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GENDER_ID", nullable = false)
	private Gender gender;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DESIGNATION_ID", nullable = false)
	private Designation designation;
	
	@Column(name="SALARY", nullable=false)
	private int salary;
	
	@Column(name="AGE", nullable=false)
	private int age;
	
	@Column(name="DATE_OF_BIRTH", nullable=false)
	private Date dateOfBirth;
	
	@JoinTable(name = "staff_skill", 
            joinColumns = { 
                   @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID")
            }, 
            inverseJoinColumns = { 
                   @JoinColumn(name = "SKILL_ID", referencedColumnName = "SKILL_ID")
            }
     )
    @ManyToMany
    private Set<Skill> skillSet = new HashSet<Skill>();;
	
	/*@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "staff")
	private Set<StaffSkill> staffSkillSet = new HashSet<StaffSkill>();*/
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "staff")
	private Set<StaffLocation> staffLocationSet = new HashSet<StaffLocation>();

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "staff")
	private Set<StaffShift> staffShiftSet = new HashSet<StaffShift>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "staff")
	private Set<StaffTraining> staffTrainingSet = new HashSet<StaffTraining>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "staff")
	private Set<StaffLeave> staffLeaveSet = new HashSet<StaffLeave>();
	
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

/*	public Set<StaffSkill> getStaffSkillSet() {
		return staffSkillSet;
	}

	public void setStaffSkillSet(Set<StaffSkill> staffSkillSet) {
		this.staffSkillSet = staffSkillSet;
	}*/

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
	
	
}
