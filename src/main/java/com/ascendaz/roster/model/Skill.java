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
@Table(name="skill")
public class Skill {
	
	@Id
	@GeneratedValue
	@Column(name="GENDER_ID", nullable=false)
	private int id;
	
	@Column(name="SKILL", nullable=false, length=50)
	private String skill;
	
	@Column(name="SEQUENCE", nullable=false)
	private int sequence;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "skill")
	private Set<StaffSkill> staffSkillSet = new HashSet<StaffSkill>();
}
