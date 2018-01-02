package com.ceiba.bl.parking.models;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parking {
	
	private String id;
	private String name;
	private List<Bill> bills;
	private Set<String> vehicles;
	// key - vehicleType / value = data
	private Map<String, VehicleType> types;

	public Parking() {
		
	}
	
	public Parking(String id, String name, List<Bill> bills, Set<String> vehicles, Map<String, VehicleType> types) {
		super();
		this.id = id;
		this.name = name;
		this.bills = bills;
		this.vehicles = vehicles;
		this.types = types;
	}

	public Map<String, VehicleType> getTypes() {
		return types;
	}

	public void setTypes(Map<String, VehicleType> types) {
		this.types = types;
	}

	public Set<String> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Set<String> vehicles) {
		this.vehicles = vehicles;
	}

	public List<Bill> getBills() {
		return bills;
	}
	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
