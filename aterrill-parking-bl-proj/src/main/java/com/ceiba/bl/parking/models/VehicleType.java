package com.ceiba.bl.parking.models;

import java.util.Map;

/**
 * {
 *   parking: {
 *   	vehicles: {
 *   		"CAR": {
 *   			capacity : 20,
 *   			count: 0,
 *   			priceTable : {
 *   				"HOUR": 1000,
 *   				"DAY": 2000
 *   			}
 *   		},
 *   		"MOTORCYCLE": {
 *   			capacity : 20,
 *   			count: 0,
 *   			priceTable : {
 *   				"HOUR": 1000,
 *   				"DAY": 2000
 *   			}
 *   		}
 *   	}
 *   }
 * }
 * 
 * @author alejandro.terrill
 *
 */
public class VehicleType {

	//value= key = unitTime - value valueUnitTime
	private Map <String, Float> pricesTable;
	private int countVehicles;
	private int capacity;
	
	public VehicleType () {
		
	}
		
	public VehicleType(Map<String, Float> pricesTable, int countVehicles, int capacity) {
		super();
		this.pricesTable = pricesTable;
		this.countVehicles = countVehicles;
		this.capacity = capacity;
	}

	public VehicleType (Map<String, Float> pricesTable) {
		this.pricesTable = pricesTable;
	}
	public Map<String, Float> getPricesTable() {
		return pricesTable;
	}

	public void setPricesTable(Map<String, Float> pricesTable) {
		this.pricesTable = pricesTable;
	}

	public int getCountVehicles() {
		return countVehicles;
	}

	public void setCountVehicles(int countVehicles) {
		this.countVehicles = countVehicles;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
