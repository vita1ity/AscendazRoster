package com.ascendaz.roster.util;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

public class Util {
	
	public static final DateTimeZone jodaTzUTC = DateTimeZone.forID("UTC");
	
	public static <T>  T deproxy (T obj) {
        if (obj == null)
            return obj;
        if (obj instanceof HibernateProxy) {
            // Unwrap Proxy;
            //      -- loading, if necessary.
            HibernateProxy proxy = (HibernateProxy) obj;
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            return (T)  li.getImplementation();
        } 
        return obj;
    }


    public static boolean isProxy (Object obj) {
        if (obj instanceof HibernateProxy)
            return true;
        return false;
    }
    

    // from  java.sql.Date  to LocalDate:
    public static LocalDate dateToLocalDate(java.sql.Date d) {
        if(d==null) return null;
        return new LocalDate(d.getTime(), jodaTzUTC);
    }

    // from  LocalDate to java.sql.Date:
    public static java.sql.Date localdateToDate(LocalDate ld) {
        if(ld==null) return null;
        return new java.sql.Date(
             ld.toDateTimeAtStartOfDay(jodaTzUTC).getMillis());
    }
}
