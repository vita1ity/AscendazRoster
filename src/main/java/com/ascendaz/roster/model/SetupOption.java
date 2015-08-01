package com.ascendaz.roster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "setup_option")
@NamedQueries({
	@NamedQuery(name = SetupOption.GET_OPTIONS_BY_IS_SELECETED, query = "SELECT option "
												+ "FROM SetupOption option "
												+ "WHERE option.isSelected = :isSelected"),
	@NamedQuery(name = SetupOption.SET_SELECTED_TRUE, query = "UPDATE SetupOption option "
												+ "SET option.isSelected = true "
												+ "WHERE option.setupOption = :setupOption"),
	@NamedQuery(name = SetupOption.SET_SELECTED_FALSE, query = "UPDATE SetupOption option "
			+ "SET option.isSelected = false "
			+ "WHERE option.setupOption = :setupOption"),
	
})
public class SetupOption {
	
	public static final String GET_OPTIONS_BY_IS_SELECETED = "getOptionsByIsSelected";
	public static final String SET_SELECTED_TRUE = "setSelectedTrue";
	public static final String SET_SELECTED_FALSE = "setSelectedFalse";
	
	@Id
	@Column(name = "OPTION_ID", nullable = false)
	private int id;
	
	@Column(name = "SETUP_OPTION", nullable = false, length = 50)
	private String setupOption;
	
	@Column(name = "IS_SELECTED", nullable = true)
	private boolean isSelected;

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
	
	@Override
	public String toString() {
		return setupOption;
	}
}
