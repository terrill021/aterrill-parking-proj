package com.ceiba.bl.parking.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ceiba.bl.parking.databuilders.BillDataBuilder;
import com.ceiba.bl.parking.databuilders.ParkingDataBuilder;
import com.ceiba.bl.parking.models.Bill;
import com.ceiba.bl.parking.models.Parking;
import com.ceiba.bl.parking.models.Vehicle;
import com.ceiba.bl.parking.models.VehicleType;
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
	
	@Test
	public void testRegisterVehicleAlreadyExist() throws Exception {
		
		// Arrange
		vehicle = new Vehicle("321", "bcd-123", "CAR", 80f);

		Mockito.when(iDbNoSql.save(bill)).thenReturn(true);
		Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking);
		Mockito.when(iDateUtilities.getDayOfWeek()).thenReturn(Calendar.WEDNESDAY);
		Mockito.when(iDateUtilities.getDateStamp()).thenReturn(Calendar.getInstance().getTime());
		
		// act
		Bill bill = ParkingImpl.registerVehicle(parking.getId(), vehicle);
		try {
			ParkingImpl.registerVehicle(parking.getId(), vehicle);
		} catch (Exception e) {
			//assert
			assertEquals("There is already an vehicle whit this license plate", e.getMessage());
		}	
	}
	
	/**
	 * When vehicle licensePlate starts by A and day of week is sunday o monday
	 * then don´t let to in the car.
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testRegisterVehicleFailByDate() throws Exception {
		
		// Arrange
		vehicle = new Vehicle("321", "acd-123", "CAR", 80f);
		Mockito.when(iDbNoSql.save(bill)).thenReturn(true);
		Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking);
		Mockito.when(iDateUtilities.getDayOfWeek()).thenReturn(Calendar.SUNDAY);
		Mockito.when(iDateUtilities.getDateStamp()).thenReturn(Calendar.getInstance().getTime());
		// act
		try {
			ParkingImpl.registerVehicle(parking.getId(), vehicle);
		} catch (Exception e) {
			assertEquals("You are not authorized to in on sundays or mondays", e.getMessage());
			throw e;
		}		
	}
	

	@Test(expected=Exception.class)
	public void testRegisterCarFailNoCapacity() throws Exception {
		Map<String, VehicleType> types;
		types = new LinkedHashMap<>();
		
		types.put("CAR", new VehicleType(null, 20, 20));
		
		parking = new ParkingDataBuilder().setTypes(types).build();
		// Arrange
		vehicle = new Vehicle("321", "acd-123", "CAR", 80f);
		Mockito.when(iDbNoSql.save(bill)).thenReturn(true);
		Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking);
		Mockito.when(iDateUtilities.getDayOfWeek()).thenReturn(Calendar.SUNDAY);
		Mockito.when(iDateUtilities.getDateStamp()).thenReturn(Calendar.getInstance().getTime());
		// act
		try {
			ParkingImpl.registerVehicle(parking.getId(), vehicle);
		} catch (Exception e) {
			assertEquals("There is not capacity for cars", e.getMessage());
			throw e;
		}		
	}

	/**
	 * *Si la moto permaneció 10 horas y es de 650CC se cobra 6.000
	 * @throws Exception 
	 */
	@Test
	public void testCharge() throws Exception {
		
		// Arrange		
		vehicle = new Vehicle("test", "abc-123", "MOTORCYCLE", 650f);
		
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
		fieldValues.put("vehicle.licensePlate", vehicle.getLicensePlate());
		fieldValues.put("state", "true");
				
		Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking); 
		Mockito.when(iDbNoSql.findByFieldValues(fieldValues, Bill.class)).thenReturn(bills);
		Mockito.when(iDbNoSql.saveOrUpdate(bill)).thenReturn(true); 
		Mockito.when(this.iDateUtilities.getDateStamp()).thenReturn(bill.getDateOut());
		Mockito.when(this.iDateUtilities.calculateNumMinutesBetweenDates(bill.getDateIn(), bill.getDateOut())).thenReturn(60L*10L);
		
		try {
			// act
			bill = ParkingImpl.charge(parking.getId(), vehicle.getLicensePlate());
			//assert
			System.out.println("Total moto: " + bill.getValue());
			assertEquals(new Double(6000), bill.getValue());
		} catch (Exception e) {
		}		
	}

	@Test
	public void testChargeFailUnregisteredVehicle() throws Exception {
		
		// Arrange		
		vehicle = new Vehicle("test", "abc-123", "MOTORCYCLE", 650f);
		
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
		fieldValues.put("vehicle.licensePlate", vehicle.getLicensePlate());
		fieldValues.put("state", "true");
				
		Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking); 
		Mockito.when(iDbNoSql.findByFieldValues(fieldValues, Bill.class)).thenReturn(bills);
		Mockito.when(iDbNoSql.saveOrUpdate(bill)).thenReturn(true); 
		Mockito.when(this.iDateUtilities.getDateStamp()).thenReturn(bill.getDateOut());
		Mockito.when(this.iDateUtilities.calculateNumMinutesBetweenDates(bill.getDateIn(), bill.getDateOut())).thenReturn(60L*10L);
		
		try {
			// act
			bill = ParkingImpl.charge(parking.getId(), "null-license");
		} catch (Exception e) {
			assertEquals("There is not registered car with this license plate", e.getMessage());
		}		
	}
	
	@Test
	public void testChargeFailUnregisteredVehicleType() throws Exception {
		
		// Arrange		
		vehicle = new Vehicle("test", "abc-123", "TRICYCLE", 650f);
		
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
		fieldValues.put("vehicle.licensePlate", vehicle.getLicensePlate());
		fieldValues.put("state", "true");
				
		Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking); 
		Mockito.when(iDbNoSql.findByFieldValues(fieldValues, Bill.class)).thenReturn(bills);
		Mockito.when(iDbNoSql.saveOrUpdate(bill)).thenReturn(true); 
		Mockito.when(this.iDateUtilities.getDateStamp()).thenReturn(bill.getDateOut());
		Mockito.when(this.iDateUtilities.calculateNumMinutesBetweenDates(bill.getDateIn(), bill.getDateOut())).thenReturn(60L*10L);
		
		try {
			// act
			bill = ParkingImpl.charge(parking.getId(), vehicle.getLicensePlate());
		} catch (Exception e) {
			assertEquals("Prices table not found for vehicle type", e.getMessage());
		}		
	}
	
	/**
	 * Calculate motorcycle value test
	 * whit out agregated values
	 */
	@Test
	public void calculateBillValueMotorcycleTest() {
		// arrange
		Double value;
		// act			
		value = ParkingImpl.calculateBillBalue(10D, parking.getTypes().get("MOTORCYCLE").getPricesTable());		
		System.out.println("Test value: " + value);
		// assert
		assertEquals(0, Double.compare(4000f, value));		
	}
	
	/**
	 * Calculate 
	 */
	@Test
	public void calculateBillValueCarTest() {		
		// arrange
		Double value;
		// act
		value = ParkingImpl.calculateBillBalue(27D, parking.getTypes().get("CAR").getPricesTable());		
		//assert
		System.out.println("Test car value: " + value);
		assertEquals(0, Double.compare(11000f, value));	
	}
	
	@Test
	public void searchVehicleTest() throws Exception{
		
		// Arrange
		Mockito.when(iDbNoSql.findOne(parking.getId(), Parking.class)).thenReturn(parking);
		// Act
		Set<String> res = ParkingImpl.searchVehicles(parking.getId());
		
		// Assert
		assertEquals(parking.getVehicles(), res);
	}
	
	@Test
	public void registerParkingTest() throws Exception{
		// arrange
		Mockito.when(iDbNoSql.save(parking)).thenReturn(true);
		
		//act
		Parking res = ParkingImpl.registerParking(parking);
		
		// assert
		assertEquals(res.getName(), res.getName());
	}

}
