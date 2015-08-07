package com.ascendaz.roster.util;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

public class Util {
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
}
