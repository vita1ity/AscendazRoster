package com.ascendaz.roster.model.attributes;

public interface Range {
	
	public Number getStartValue();
	public Number getEndValue();
	public boolean checkInBetween(Comparable<Number> object);
	
}
