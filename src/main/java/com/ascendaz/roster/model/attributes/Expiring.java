package com.ascendaz.roster.model.attributes;

import java.util.Date;

public interface Expiring {
	
	public boolean checkExpired(Date currentDate);
	
}
