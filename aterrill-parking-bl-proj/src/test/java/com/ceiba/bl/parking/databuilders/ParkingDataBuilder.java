package com.ceiba.bl.parking.databuilders;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ceiba.bl.parking.models.Bill;
import com.ceiba.bl.parking.models.Parking;
import com.ceiba.bl.parking.models.VehicleType;

public class ParkingDataBuilder {

	private String id;
	private String name;
	private List<Bill> bills;
	private Set<String> vehicles;
	// key - vehicleType / value = data
	private Map<String, VehicleType> types;
	private Map <String, Float> pricesTableCars;
	private Map <String, Float> pricesTableMotorcycles;
	
	
	public ParkingDataBuilder() {
		this.id = "21";
		this.name = "Parking-aterrill";
		bills = new ArrayList<>();
		types = new LinkedHashMap<>();
		pricesTableCars = new LinkedHashMap<>();
		pricesTableMotorcycles = new LinkedHashMap<>();
		vehicles = new LinkedHashSet<>();
		
		pricesTableCars.put("HOUR", 1000f);
		pricesTableCars.put("DAY", 8000f);

		pricesTableMotorcycles.put("HOUR", 500f);
		pricesTableMotorcycles.put("DAY", 4000f);

		types.put("CAR", new VehicleType(pricesTableCars, 0, 20));
		types.put("MOTORCYCLE", new VehicleType(pricesTableMotorcycles, 0, 10));
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<Bill> getBills() {
		return bills;
	}

	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}

	
	public void setVehicles(Set<String> vehicles) {
		this.vehicles = vehicles;
	}

	public ParkingDataBuilder setTypes(Map<String, VehicleType> types) {
		this.types = types;
		return this;
	}

	public ParkingDataBuilder setPricesTableCars(Map<String, Float> pricesTableCars) {
		this.pricesTableCars = pricesTableCars;
		return this;
	}

	public void setPricesTableMotorcycles(Map<String, Float> pricesTableMotorcycles) {
		this.pricesTableMotorcycles = pricesTableMotorcycles;
	}

	public Parking build() {
		Parking parking = new Parking(id, name, bills, vehicles, types);
		return parking;
	}
}
