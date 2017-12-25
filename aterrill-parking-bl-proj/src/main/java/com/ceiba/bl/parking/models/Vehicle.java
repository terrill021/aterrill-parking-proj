package com.ceiba.bl.parking.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * Vehiculo con motor
 * @author alejandro.terrill
 *
 */

public class Vehicle {

	private String id;
	
	private String licensePlate;
	private String type;
	private Float displacement;
			
	public Vehicle() {
		super();
	}

	public Vehicle(String id, String licensePlate, String type, Float displacement) {
		super();
		this.id = id;
		this.licensePlate = licensePlate;
		this.type = type;
		this.displacement = displacement;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getDisplacement() {
		return displacement;
	}

	public void setDisplacement(Float displacement) {
		this.displacement = displacement;
	}
}
