package com.ceiba.bl.parking.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceiba.bl.parking.IParking;
import com.ceiba.bl.parking.models.Bill;
import com.ceiba.bl.parking.models.Parking;
import com.ceiba.bl.parking.models.Vehicle;
import com.ceiba.bl.parking.models.VehicleType;
import com.ceiba.repository.nosqldb.IDbNoSql;
import com.ceiba.utilities.IDateUtilities;

@Service
public class ParkingImpl implements IParking{

	@Autowired
	IDbNoSql iDbNoSql;
	
	@Autowired
	IDateUtilities iDateUtilities;
	
	@Override
	public Bill registerVehicle(String parkingId, Vehicle vehicle) throws Exception {
		
		Parking parking = searchParking(parkingId);		
		String type = vehicle.getType();
		if(parking.getTypes().get(type).getCapacity() <= parking.getTypes().get(type).getCountVehicles()) {
			throw new Exception("There is not capacity for cars");
		}
		if (vehicle.getLicensePlate().substring(0, 1).equalsIgnoreCase("a")
				&& iDateUtilities.getDayOfWeek() == Calendar.SUNDAY 
				|| iDateUtilities.getDayOfWeek() == Calendar.MONDAY) {
				throw new Exception("You are not authorized to in on sundays or mondays");
		}
		Bill bill = new Bill();
		bill.setVehicle(vehicle);
		bill.setState("true");		
		bill.setDateIn(iDateUtilities.getDateStamp());
		bill.setParkingId(parking.getId());
		boolean operationResult = false;
		operationResult = parking.getVehicles().add(vehicle.getLicensePlate());
		if (!operationResult) {
			throw new Exception("There is already an vehicle whit this license plate");
		}
		iDbNoSql.save(bill);
		parking.getBills().add(bill);		
		iDbNoSql.saveOrUpdate(parking);
		return bill;
	}
	
	@Override
	public synchronized Bill charge(String parkingId, String licensePlate) throws Exception{
		Parking parking = searchParking(parkingId);
		Map<String, String> fieldValues = new HashMap<>();
		fieldValues.put("vehicle.licensePlate", licensePlate);
		fieldValues.put("state", "true");
		List<Bill> bills  = iDbNoSql.findByFieldValues(fieldValues, Bill.class);
		if (bills == null || bills.isEmpty()) {
			throw new Exception("There is not registered car with this license plate");
		}
		Bill bill = bills.get(0);
		final String type = bill.getVehicle().getType();
		Double subTotal = 0d;
		if (type.equalsIgnoreCase("MOTORCYCLE") &&
				bill.getVehicle().getDisplacement() > 500) {
			subTotal += 2000;
		}
		Long numMinutes = iDateUtilities.calculateNumMinutesBetweenDates(bill.getDateIn(), iDateUtilities.getDateStamp()); 
		Double numHours = Math.ceil(numMinutes / 60.0);
		Map<String, Float> pricesTable;
		VehicleType vehicle = parking.getTypes().get(type);
		if (vehicle == null || vehicle.getPricesTable().isEmpty()) {
			throw new Exception ("Prices table not found for vehicle type");
		}
		pricesTable = vehicle.getPricesTable(); 
		 
		bill.setValue(calculateBillBalue(numHours, pricesTable) + subTotal);
		bill.setState("false");		
		parking.getVehicles().remove(bill.getVehicle().getLicensePlate());		
		iDbNoSql.saveOrUpdate(parking);
		iDbNoSql.saveOrUpdate(bill);		
		return bill;
	}	
	

	@Override
	public Parking registerParking(Parking parking) throws Exception {
		iDbNoSql.save(parking);		
		return parking;
	}
	

	@Override
	public Set<String> searchVehicles (String parkingId) throws Exception {
		Parking parking = searchParking(parkingId);
		return parking.getVehicles();
	}
	private Parking searchParking(String parkingId) throws Exception {
		Parking parking = iDbNoSql.findOne(parkingId, Parking.class);
		
		if (parking != null){ return parking;} else throw new Exception("Parking with id " + parkingId + " not found");
	}
	
	/**
	 * Calculate bill value.
	 * @param numHours number of hours in parking
	 * @param pricesTable Map prices with unit times and values
	 * @return bill value by time and price table
	 */
	public Double calculateBillBalue(Double numHours, Map<String, Float> pricesTable) {
		Double value = 0D;
		
		if (numHours > 0) {
			if (numHours <= 9) {
				value += (numHours * pricesTable.get("HOUR"));
				return value;
			}
			value += pricesTable.get("DAY");
			numHours -= 24;
			value += calculateBillBalue(numHours, pricesTable);
		}
		
		return value;
	}

	public void setiDbNoSql(IDbNoSql iDbNoSql) {
		this.iDbNoSql = iDbNoSql;
	}

	public void setiDateUtilities(IDateUtilities iDateUtilities) {
		this.iDateUtilities = iDateUtilities;
	}
	
}
