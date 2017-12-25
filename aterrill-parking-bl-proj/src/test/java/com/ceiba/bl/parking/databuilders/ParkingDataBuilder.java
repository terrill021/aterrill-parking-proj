package com.ceiba.bl.parking.databuilders;

import java.util.ArrayList;
import java.util.List;

import com.ceiba.bl.parking.models.Bill;
import com.ceiba.bl.parking.models.Parking;
import com.ceiba.bl.parking.models.PriceTable;
import com.ceiba.bl.parking.models.Vehicle;

public class ParkingDataBuilder {

	private String id;
	private String name;
	private int carsCapacity;
	private int motorcyclesCapacity;
	private PriceTable priceTable;
	private List<Bill> bills;
	private List<Vehicle> cars;
	private List<Vehicle> motorcycles;
	
	public ParkingDataBuilder() {
		this.id = "id_parking";
		this.name = "Parking-test";
		this.carsCapacity = 20;
		this.motorcyclesCapacity = 10;
		this.priceTable = new PriceTableDataBuilder().build();
		bills = new ArrayList<>();
		cars = new ArrayList<>();
		motorcycles = new ArrayList<>();
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

	public int getCarsCapacity() {
		return carsCapacity;
	}

	public void setCarsCapacity(int carsCapacity) {
		this.carsCapacity = carsCapacity;
	}

	public int getMotorcyclesCapacity() {
		return motorcyclesCapacity;
	}

	public void setMotorcyclesCapacity(int motorcyclesCapacity) {
		this.motorcyclesCapacity = motorcyclesCapacity;
	}

	public PriceTable getPriceTable() {
		return priceTable;
	}

	public void setPriceTable(PriceTable priceTable) {
		this.priceTable = priceTable;
	}

	public List<Bill> getBills() {
		return bills;
	}

	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}

	public List<Vehicle> getCars() {
		return cars;
	}

	public void setCars(List<Vehicle> cars) {
		this.cars = cars;
	}

	public List<Vehicle> getMotorcycles() {
		return motorcycles;
	}

	public void setMotorcycles(List<Vehicle> motorcycles) {
		this.motorcycles = motorcycles;
	}

	public Parking build() {
		Parking parking = new Parking(id, name, carsCapacity, motorcyclesCapacity, priceTable, bills, cars, motorcycles);
		return parking;
	}
}
