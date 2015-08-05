package com.ascendaz.roster.model.config;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "roster_rule")
@NamedNativeQuery(name=Rule.SAVE_RULE, query="UPDATE roster_rule "
											+ "SET CRITERIA = :criteria, TYPE = :type, PRIORITY = :priority, IS_SELECTED = 1 "
											+ "WHERE SETUP_OPTION_ID = (SELECT OPTION_ID "
																	+ "FROM setup_option "
																	+ "WHERE SETUP_OPTION = :base)")
											
@NamedQueries({
	@NamedQuery(name = Rule.GET_RULE_BY_OPTION, query = "SELECT rule1 "
												+ "FROM Rule rule1 "
												+ "WHERE rule1.setupOption.id = :setupOptionId"),
	
	
	/*@NamedQuery(name = SetupOption.SET_SELECTED_TRUE, query = "UPDATE SetupOption option "
												+ "SET option.isSelected = true "
												+ "WHERE option.setupOption = :setupOption"),
	@NamedQuery(name = SetupOption.SET_SELECTED_FALSE, query = "UPDATE SetupOption option "
			+ "SET option.isSelected = false "
			+ "WHERE option.setupOption = :setupOption"),*/
	
})
public class Rule implements Serializable, Comparable<Rule>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7862186534373258869L;

	public static final String GET_RULE_BY_OPTION = "getRuleByOption";
	public static final String SAVE_RULE = "updateRule";
	
	@Id
	@Column(name = "RULE_ID", nullable = false, unique = true)
	private int id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SETUP_OPTION_ID", nullable = false)
	@JsonManagedReference
	private SetupOption setupOption;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REFERENCE_ID", nullable = false)
	@JsonManagedReference
	private Reference reference;
	
	@Column(name = "CRITERIA", nullable = true, length = 20)
	private Criteria criteria;
	
	@Column(name = "TYPE", nullable = true, length = 4)
	private RuleType type;
	
	@Column(name = "PRIORITY", nullable = true)
	private Integer priority;
	
	@Column(name = "IS_SELECTED", nullable = true)
	private Boolean isSelected;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SetupOption getSetupOption() {
		return setupOption;
	}

	public void setSetupOption(SetupOption setupOption) {
		this.setupOption = setupOption;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public RuleType getType() {
		return type;
	}

	public void setType(RuleType type) {
		this.type = type;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean isSelected() {
		return isSelected;
	}

	public void setSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	@Override
	public String toString() {
		return "Rule [id=" + id + ", setupOption=" + setupOption + ", reference=" + reference + ", criteria=" + criteria
				+ ", type=" + type + ", priority=" + priority + ", isSelected=" + isSelected + "]";
	}

	@Override
	public int compareTo(Rule rule) {
		return this.priority - rule.getPriority();
	}
}
