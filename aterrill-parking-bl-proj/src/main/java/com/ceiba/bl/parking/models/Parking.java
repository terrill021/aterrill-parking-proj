package com.ceiba.bl.parking.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public class Parking {
	
	private String id;
	private String name;
	private int carsCapacity;
	private int motorcyclesCapacity;
	private PriceTable priceTable;
	private List<Bill> bills;
	private List<Vehicle> cars;
	private List<Vehicle> motorcycles;
	
	public Parking(String id, String name, int carsCapacity, int motorcyclesCapacity, PriceTable priceTable,
			List<Bill> bills, List<Vehicle> cars, List<Vehicle> motorcycles) {
		super();
		this.id = id;
		this.name = name;
		this.carsCapacity = carsCapacity;
		this.motorcyclesCapacity = motorcyclesCapacity;
		this.priceTable = priceTable;
		this.bills = bills;
		this.cars = cars;
		this.motorcycles = motorcycles;
	}
	
	
	public Parking() {
		super();
	}


	public List<Bill> getBills() {
		return bills;
	}
	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}
	public PriceTable getPriceTable() {
		return this.priceTable;
	}
	public void setPriceTable(PriceTable priceTable) {
		this.priceTable = priceTable;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
