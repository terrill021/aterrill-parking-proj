package com.ceiba.bl.parking.databuilders;

import java.util.Calendar;
import java.util.Date;

import com.ceiba.bl.parking.models.Bill;
import com.ceiba.bl.parking.models.Vehicle;

public class BillDataBuilder {

	private String id;
	private Date dateIn;
	private Date dateOut;
	private String parkingId;
	private Vehicle vehicle;
	
	private Double value;
	private String state;
	
	public BillDataBuilder () {
		this.id = "bill-test-id";
		this.dateIn = Calendar.getInstance().getTime();
		this.dateOut = Calendar.getInstance().getTime();
		this.vehicle = new Vehicle();
		this.value = 1000d;
		this.state = "true";
	}
	
	public Bill build() {
		return new Bill(this.id, this.dateIn, dateOut, this.parkingId, this.vehicle, this.value, this.state);
	}

	String getId() {
		return id;
	}

	public BillDataBuilder setId(String id) {
		this.id = id;
		return this;
	}

	public Date getDateIn() {
		return dateIn;
	}

	public BillDataBuilder setDateIn(Date dateIn) {
		this.dateIn = dateIn;
		return this;
	}

	public Date getDateOut() {
		return dateOut;
	}

	public BillDataBuilder setDateOut(Date dateOut) {
		this.dateOut = dateOut;
		return this;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public BillDataBuilder setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
		return this;
	}

	public Double getValue() {
		return value;
	}

	public BillDataBuilder setValue(Double value) {
		this.value = value;
		return this;
	}

	public String getState() {
		return state;
	}

	public BillDataBuilder setState(String state) {
		this.state = state;
		return this;
	}

	public String getParkingId() {
		return parkingId;
	}

	public BillDataBuilder setParkingId(String parkingId) {
		this.parkingId = parkingId;
		return this;
	}
	
	
}
