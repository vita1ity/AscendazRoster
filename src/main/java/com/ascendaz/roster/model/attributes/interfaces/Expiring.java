package com.ascendaz.roster.model.attributes.interfaces;

import org.joda.time.LocalDate;

public interface Expiring {
	
	public boolean checkExpired(LocalDate currentDate);
	
	public Object getValueObject();
	
}
