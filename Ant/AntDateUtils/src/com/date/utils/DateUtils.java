package com.date.utils;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * If no options, the default target will be executed, in this example, the
 * default target is 'main'.
 * 
 * 5.1 Run a class inside a Jar file : $ java -cp dist/DateUtils-20141030.jar
 * com.mkyong.core.utils.DateUtils
 * 
 * 5.2 Run the executable Jar file : $ java -jar dist/DateUtils-20141030.jar
 * 
 * @author Administrator
 * 
 */
 
public class DateUtils {
 
	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
 
	public static void main(String[] args) {
 
		logger.debug("[MAIN] Current Date : {}", getLocalCurrentDate());
		System.out.println(getLocalCurrentDate());
 
	}
 
	private static String getLocalCurrentDate() {
 
		LocalDate date = new LocalDate();
		return date.toString();
 
	}
}
