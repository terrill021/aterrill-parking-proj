package com.ceiba.bl.parking.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ceiba.bl.parking.databuilders.BillDataBuilder;
import com.ceiba.bl.parking.databuilders.ParkingDataBuilder;
import com.ceiba.bl.parking.databuilders.PriceTableDataBuilder;
import com.ceiba.bl.parking.models.Bill;
import com.ceiba.bl.parking.models.Parking;
import com.ceiba.bl.parking.models.PriceTable;
import com.ceiba.bl.parking.models.Vehicle;
import com.ceiba.repository.nosqldb.IDbNoSql;
import com.ceiba.utilities.IDateUtilities;

public class ParkingImplTest {

	IDbNoSql iDbNoSql;
	ParkingImpl ParkingImpl;
	Parking parking;
	Vehicle vehicle;
	Bill bill;
	IDateUtilities iDateUtilities;
	
	@Before 
	public void before() {
		iDbNoSql = Mockito.mock(IDbNoSql.class);
		iDateUtilities = Mockito.mock(IDateUtilities.class);
		ParkingImpl = new ParkingImpl();
		ParkingImpl.setiDbNoSql(iDbNoSql);
		ParkingImpl.setiDateUtilities(iDateUtilities);
		bill = new Bill();
		parking = new ParkingDataBuilder().build();
	}
	
	@Test
	public void testRegisterVehicle() throws Exception {
		
		// Arrange
		vehicle = new Vehicle("321", "bcd-123", "CAR", 80f);

		Mockito.when(iDbNoSql.save(bill)).thenReturn(true);
		Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking);
		Mockito.when(iDateUtilities.getDayOfWeek()).thenReturn(Calendar.WEDNESDAY);
		Mockito.when(iDateUtilities.getDateStamp()).thenReturn(Calendar.getInstance().getTime());
		
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

		Mockito.when(iDbNoSql.save(bill)).thenReturn(true);
		Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking);
		Mockito.when(iDateUtilities.getDayOfWeek()).thenReturn(Calendar.SUNDAY);
		Mockito.when(iDateUtilities.getDateStamp()).thenReturn(Calendar.getInstance().getTime());
		vehicle.setLicensePlate("ABC-123");
		
		// act
		try {
			Bill bill = ParkingImpl.registerVehicle(parking.getId(), vehicle);
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
	public void testCharge() throws Exception {
		
		// Arrange		
		vehicle = new Vehicle("test", "", "MOTORCYCLE", 650f);
		
		Calendar dateIn = Calendar.getInstance();
		dateIn.set(2017, Calendar.DECEMBER, 21, 0, 0);
		
		Calendar dateOut = Calendar.getInstance();
		dateIn.set(2017, Calendar.DECEMBER, 21, 10, 0);
		
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
				
		Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking); 
		Mockito.when(iDbNoSql.findByFieldValues(fieldValues, Bill.class)).thenReturn(bills);
		Mockito.when(iDbNoSql.saveOrUpdate(bill)).thenReturn(true); 
		Mockito.when(this.iDateUtilities.calculateNumMinutesBetweenDates(bill.getDateIn(), bill.getDateOut())).thenReturn(60L*10L);
		
		try {
			// act
			bill = ParkingImpl.charge(parking.getId(), vehicle.getLicensePlate());
			//assert
			System.out.println("Total moto: " + bill.getValue());
			assertEquals(new Double(6000), bill.getValue());
		} catch (Exception e) {
			fail();
		}
		
	}

	/**
	 * Calculate motorcycle value test
	 */
	@Test
	public void calculateBillValueMotorcycleTest() {
		
		PriceTable priceTable = new PriceTableDataBuilder().build();
		
		Double value = ParkingImpl.calculateBillBalue(10D, priceTable.getPricesTable().get("MOTORCYCLE"));
		
		System.out.println("Test value: " + value);
		assertEquals(Double.compare(4000f, value), 0);
	
		
	}
	
	/**
	 * Calculate 
	 */
	@Test
	public void calculateBillValueCarTest() {
		
		PriceTable priceTable = new PriceTableDataBuilder().build();
		
		Double value = ParkingImpl.calculateBillBalue(27D, priceTable.getPricesTable().get("CAR"));
		
		System.out.println("Test car value: " + value);
		assertEquals(Double.compare(11000f, value), 0);
	
		
	}

}
