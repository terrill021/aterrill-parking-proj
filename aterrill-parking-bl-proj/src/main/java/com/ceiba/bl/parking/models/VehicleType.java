package com.ceiba.bl.parking.models;

public enum VehicleType {

	MOTORCYCLE("MOTORCYCLE"), CAR("CAR");
	
	private VehicleType (String type) {
		this.type = type;
	}
	
	public String getType () {
		return this.type;
	}
	
	String type;
}
