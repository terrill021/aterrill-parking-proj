package com.ceiba.utilities;

import java.util.Date;

public interface IDateUtilities {

	Long calculateNumMinutesBetweenDates (Date dateStart, Date datEnd);
	
	int getDayOfWeek();
	
	Date getDateStamp();
}
