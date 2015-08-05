package com.ascendaz.roster.model.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "reference")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5479094945708113401L;

	@Id
	@Column(name = "REFERENCE_ID", nullable = false, unique = true)
	private int id;
	
	@Column(name = "REFERENCE", nullable = false, length = 50)
	private String reference;
	
	@Column(name = "ATTRIBUTE_NAME", nullable = true)
	private String attributeName;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "reference")
	@JsonBackReference
	private List<Rule> ruleList = new ArrayList<Rule>();

	

	public List<Rule> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<Rule> ruleList) {
		this.ruleList = ruleList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	
	
}
