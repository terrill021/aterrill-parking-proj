package com.ceiba.bl.parking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ceiba.utilities.IDateUtilities;
import com.ceiba.utilities.impl.DateUtilitiesImpl;

@Configuration
public class UtilitiesConfig {

	@Bean
	public IDateUtilities iDateUtilities() {
		
		return new DateUtilitiesImpl();
	}
}
