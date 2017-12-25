package com.ceiba.utilities.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ceiba.utilities.IDateUtilities;

import javafx.geometry.HorizontalDirection;

public class DateUtilitiesImpl implements IDateUtilities{

	@Override
	public Long calculateNumMinutesBetweenDates(Date startDate, Date endDate) {

		long diff = endDate.getTime() - startDate.getTime();
		long diffMinutes = diff / (60 * 1000);	
		return diffMinutes;
	}

	@Override
	public int getDayOfWeek() {
		
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	@Override
	public Date getDateStamp() {
		
		return Calendar.getInstance().getTime();
	}
	
		
}
