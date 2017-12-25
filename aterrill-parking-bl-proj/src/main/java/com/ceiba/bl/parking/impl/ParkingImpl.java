package com.ceiba.bl.parking.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceiba.bl.parking.IParking;
import com.ceiba.bl.parking.databuilders.ParkingDataBuilder;
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
		
		if(parking.getCars().size() >= parking.getCarsCapacity()) {
			throw new Exception("There is not capacity for cars");
		}
		
		if (parking.getMotorcycles().size() >= parking.getMotorcyclesCapacity()) {
			throw new Exception("There is not capacity for motorcycles");
		}		
		
		if (vehicle.getLicensePlate().substring(0, 1).equalsIgnoreCase("a")) {
			if(iDateUtilities.getDayOfWeek() == Calendar.SUNDAY || iDateUtilities.getDayOfWeek() == Calendar.MONDAY) {
				throw new Exception("You are not authorized to in on sundays or mondays");
			}
		}
				
		Bill bill = new Bill();
		
		bill.setVehicle(vehicle);
		bill.setState("true");		
		bill.setDateIn(iDateUtilities.getDateStamp());
		bill.setParkingId(parking.getId());
		iDbNoSql.save(bill);
		
		parking.getBills().add(bill);
		
		switch (vehicle.getType()) {
			case "MOTORCYCLE":
				parking.getMotorcycles().add(vehicle);
			break;
			case "CAR":
				parking.getCars().add(vehicle);
			break;
			default:
				throw new Exception("Vehicle type not found");
		}
	
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
		
		Double subTotal = 0d;
		
		if (bill.getVehicle().getType().equalsIgnoreCase(VehicleType.MOTORCYCLE.getType()) &&
				bill.getVehicle().getDisplacement() > 500) {
			subTotal += 2000;
		}
		
		Long numMinutes = iDateUtilities.calculateNumMinutesBetweenDates(bill.getDateIn(), iDateUtilities.getDateStamp()); 
		
		Double numHours = Math.ceil(numMinutes / 60.0);
		Map<String, Float> pricesTable = parking.getPriceTable().getPricesTable().get(bill.getVehicle().getType()); 
		
		bill.setValue(calculateBillBalue(numHours, pricesTable) + subTotal);
		bill.setState("false");
		iDbNoSql.saveOrUpdate(bill);		
		return bill;
	}	
	

	@Override
	public Parking registerParking(Parking parking) throws Exception {
		iDbNoSql.save(parking);		
		return parking;
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

	public IDbNoSql getiDbNoSql() {
		return iDbNoSql;
	}

	public void setiDbNoSql(IDbNoSql iDbNoSql) {
		this.iDbNoSql = iDbNoSql;
	}


	public IDateUtilities getiDateUtilities() {
		return iDateUtilities;
	}


	public void setiDateUtilities(IDateUtilities iDateUtilities) {
		this.iDateUtilities = iDateUtilities;
	}
	
}
