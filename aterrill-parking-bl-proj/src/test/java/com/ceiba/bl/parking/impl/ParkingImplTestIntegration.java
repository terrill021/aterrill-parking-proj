package com.ceiba.bl.parking.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.ceiba.bl.parking.config.RepositoryConfig;
import com.ceiba.bl.parking.databuilders.BillDataBuilder;
import com.ceiba.bl.parking.databuilders.ParkingDataBuilder;
import com.ceiba.bl.parking.models.Bill;
import com.ceiba.bl.parking.models.Parking;
import com.ceiba.bl.parking.models.Vehicle;
import com.ceiba.repository.nosqldb.IDbNoSql;
import com.ceiba.repository.nosqldb.config.MongoDbConnection;
import com.ceiba.repository.nosqldb.impl.MongoDb;
import com.ceiba.repository.nosqldb.model.DataSourceNoSql;
import com.ceiba.utilities.IDateUtilities;

public class ParkingImplTestIntegration {

	IDbNoSql iDbNoSql;
	
	ParkingImpl ParkingImpl;
	Parking parking;
	Vehicle vehicle;
	Bill bill;
	IDateUtilities iDateUtilities;
	
	@Before
	public void before() throws Exception {
		//iDbNoSql = Mockito.mock(IDbNoSql.class);
		MongoDb mongoDb = new MongoDb();
		MongoDbConnection mc = new MongoDbConnection() {			
			@Override
			public IDbNoSql iDbNoSql() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public DataSourceNoSql getDataSourceNoSql() {
				DataSourceNoSql dataSourceNoSql = new DataSourceNoSql();
				dataSourceNoSql.setDataBase("test-mongo-parking-bl");
				dataSourceNoSql.setHost("127.0.0.1");
				dataSourceNoSql.setPort(27017);
				return dataSourceNoSql;
			}
		};
		mongoDb.setMongoTemplate(mc.mongoTemplate());
		iDbNoSql = mongoDb;
		mc.mongoTemplate().getDb().dropDatabase();
		iDateUtilities = Mockito.mock(IDateUtilities.class);
		
		ParkingImpl = new ParkingImpl();
		ParkingImpl.setiDbNoSql(iDbNoSql);
		ParkingImpl.setiDateUtilities(iDateUtilities);
		bill = new Bill();
		parking = new ParkingDataBuilder().build();
	}
	
	/**
	 * Test integratrion with iDbNoSql 
	 * @throws Exception
	 */
	@Test
	public void testRegisterVehicle() throws Exception {
		
		// Arrange
		vehicle = new Vehicle("321", "bcd-123", "CAR", 80f);
		//Mockito.when(iDbNoSql.save(bill)).thenReturn(true);
		//Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking);
		Mockito.when(iDateUtilities.getDayOfWeek()).thenReturn(Calendar.WEDNESDAY);
		Mockito.when(iDateUtilities.getDateStamp()).thenReturn(Calendar.getInstance().getTime());	
		iDbNoSql.saveOrUpdate(parking);
		// act
		Bill bill = ParkingImpl.registerVehicle(parking.getId(), vehicle);
		// assert
		assertNotNull(bill);		
	}
	
	/**
	 * When vehicle licensePlate starts by A and day of week is sunday o monday
	 * then don´t let to in the car.
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testRegisterVehicleFailByDate() throws Exception {
		
		// Arrange
		vehicle = new Vehicle("321", "bcd-123", "car", 80f);
		//Mockito.when(iDbNoSql.save(bill)).thenReturn(true);
		//Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking);
		Mockito.when(iDateUtilities.getDayOfWeek()).thenReturn(Calendar.SUNDAY);
		Mockito.when(iDateUtilities.getDateStamp()).thenReturn(Calendar.getInstance().getTime());
		vehicle.setLicensePlate("ABC-123");
		iDbNoSql.saveOrUpdate(parking);
	
		// act
		try {
			Bill bill = ParkingImpl.registerVehicle(parking.getId(), vehicle);			
			//assert
			Assert.fail();
		} catch (Exception e) {
			assertEquals("You are not authorized to in on sundays or mondays", e.getMessage());
			throw e;
		}		
	}
	
	/**
	 * *Si la moto permaneció un 10 horas y es de 650CC se cobra 6.000
	 * @throws Exception 
	 */
	@Test
	public void testChargeFullIntegration() throws Exception {		
		// Arrange		
		vehicle = new Vehicle("test", "", "MOTORCYCLE", 650f);		
		Calendar dateIn = Calendar.getInstance();
		dateIn.set(2017, Calendar.DECEMBER, 21, 0, 0);		
		Calendar dateOut = Calendar.getInstance();
		dateOut.set(2017, Calendar.DECEMBER, 21, 10, 0);	
		
		parking = new ParkingDataBuilder().build();		
		this.bill = new BillDataBuilder()
				.setParkingId(parking.getId())
				.setVehicle(vehicle)
				.setDateIn(dateIn.getTime())
				.setDateOut(dateOut.getTime())
				.build();		
		List<Bill> bills = new ArrayList<>();
		bills.add(this.bill);		
		Map<String, String> fieldValues = new HashMap<>();
		fieldValues.put("licensePlate", vehicle.getLicensePlate());
		fieldValues.put("state", "true");				
		//Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking); 
		//Mockito.when(iDbNoSql.findByFieldValues(fieldValues, Bill.class)).thenReturn(bills);
		//Mockito.when(iDbNoSql.saveOrUpdate(bill)).thenReturn(true); 
		Mockito.when(this.iDateUtilities.getDateStamp()).thenReturn(dateOut.getTime());
		Mockito.when(this.iDateUtilities.calculateNumMinutesBetweenDates(bill.getDateIn(), bill.getDateOut())).thenReturn(60L*10L);
		
		iDbNoSql.saveOrUpdate(parking);
		iDbNoSql.saveOrUpdate(this.bill);		
		try {
			// act
			bill = ParkingImpl.charge(parking.getId(), vehicle.getLicensePlate());
			//assert
			assertEquals(new Double(6000), bill.getValue());
		} catch (Exception e) {
			Assert.fail();
		}		
	}

}
