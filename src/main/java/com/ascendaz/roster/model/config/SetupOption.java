package com.ascendaz.roster.model.config;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "setup_option")

public class SetupOption implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9157664777858689333L;
	
	@Id
	@Column(name = "OPTION_ID", nullable = false)
	private int id;
	
	@Column(name = "SETUP_OPTION", nullable = false, length = 50)
	private String setupOption;
	
	@Column(name = "ATTRIBUTE_NAME", nullable = true)
	private String attributeName;
	
	@Column(name = "IS_SELECTED", nullable = true)
	private boolean isSelected;
	
	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "setupOption")
	@JsonBackReference
	private Rule rule;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSetupOption() {
		return setupOption;
	}

	public void setSetupOption(String setupOption) {
		this.setupOption = setupOption;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
	
	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	
	
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
	public String toString() {
		return setupOption;
	}
}
