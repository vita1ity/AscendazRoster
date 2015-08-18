package com.ascendaz.roster.model.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum Criteria implements Serializable{
	
	EQUAL("Should be equal to"), 
    NOT_EQUAL("Should not be equal to"), 
    CONTAINS("Should contain"),
    IN_BETWEEN("In between"),
    ATLEAST("Atleast"),
    ATMOST("Atmost"); 

	@JsonProperty
    private final String criteriaString;

    Criteria(String criteriaString) {
        this.criteriaString = criteriaString;
    }
    
    public String getCriteriaString() {
        return this.criteriaString;
    }
	public static Criteria constructCriteria(String str) {
		if (str.equals("Should be equal to")) {
			return Criteria.EQUAL;
		}
		else if (str.equals("Should contain")) {
			return Criteria.CONTAINS;
		}
		else if (str.equals("Should not be equal to")) {
			return Criteria.NOT_EQUAL;
		} 
		else if (str.equals("In between")) {
			return Criteria.IN_BETWEEN;
		}
		else if (str.equals("Atleast")) {
			return Criteria.ATLEAST;
		}
		else if (str.equals("Atmost")) {
			return Criteria.ATMOST;
		} 
		else return null;
	}
}
