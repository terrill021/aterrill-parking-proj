package com.ceiba.bl.parking.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.ceiba.bl.parking.config.RepositoryConfig;
import com.ceiba.bl.parking.databuilders.ParkingDataBuilder;
import com.ceiba.repository.nosqldb.IDbNoSql;

public class ParkingImplTestIntegration {

	
	IDbNoSql iDbNoSql;
	
	@Before
	public void init() {
		//iDbNoSql = new RepositoryConfig().iDbNoSql();
	}
	@Test
	public void test() throws Exception{
		
	}

}
