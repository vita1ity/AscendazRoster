package com.ascendaz.roster.model.attributes.interfaces;

public interface Range {
	
	public Number getStartValue();
	public Number getEndValue();
	public boolean checkInBetween(Comparable<Number> object);
	
}
