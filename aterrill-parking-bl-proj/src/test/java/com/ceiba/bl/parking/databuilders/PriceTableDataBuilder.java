package com.ceiba.bl.parking.databuilders;

import java.util.HashMap;
import java.util.Map;

import com.ceiba.bl.parking.models.PriceTable;

public class PriceTableDataBuilder {

	private Map <String, Map<String, Float>> pricesTable;

	public PriceTableDataBuilder() {
		pricesTable = table();
	}
	
	public PriceTable build() {
		
		return new PriceTable(pricesTable);
	}
	
	public Map<String, Float> subTableCars() {
		Map<String, Float> table = new HashMap<>();
		table.put("HOUR", 1000f);
		table.put("DAY", 8000f);
		return table;
	}
	
	public Map<String, Float> subTableMotorcycles() {
		Map<String, Float> table = new HashMap<>();
		table.put("HOUR", 500f);
		table.put("DAY", 4000f);
		return table;
	}
	
	public Map <String, Map<String, Float>> table() {
		
		Map <String, Map<String, Float>> table = new HashMap<>();
		table.put("CAR", subTableCars());
		table.put("MOTORCYCLE", subTableMotorcycles());
		
		return table;
	}
}
