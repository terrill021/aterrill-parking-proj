package com.ceiba.bl.parking.models;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

public class PriceTable {

	private Map <String, Map<String, Float>> pricesTable;

	public PriceTable () {
		
	}
	
	public PriceTable (Map<String, Map<String, Float>> pricesTable) {
		this.pricesTable = pricesTable;
	}
	public Map<String, Map<String, Float>> getPricesTable() {
		return pricesTable;
	}

	public void setPricesTable(Map<String, Map<String, Float>> pricesTable) {
		this.pricesTable = pricesTable;
	}
	
}
