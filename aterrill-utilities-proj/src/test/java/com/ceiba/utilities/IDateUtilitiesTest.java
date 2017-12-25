package com.ceiba.utilities;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.ceiba.utilities.impl.DateUtilitiesImpl;

public class IDateUtilitiesTest {

	IDateUtilities iDateUtilities;
	
	@Before
	public void init() {
		iDateUtilities = new DateUtilitiesImpl();
	}
	
	@Test
	public void calculateNumHoursBetweenDatesTest() {
		
		// Arrange
		Calendar dateStart = Calendar.getInstance();
		dateStart.set(2017, Calendar.DECEMBER, 21, 0, 0, 0);
		
		Calendar dateEnd = Calendar.getInstance();
		dateEnd.set(2017, Calendar.DECEMBER, 21, 0, 1, 0);
		
		// Act
		Long dif1 = iDateUtilities.calculateNumMinutesBetweenDates(dateStart.getTime(), dateEnd.getTime());
		
		// Assert
		assertEquals(Long.compare(dif1, 1l), 0);		
	}

}
