package com.ceiba.bl.parking;

import java.util.List;
import java.util.Set;

import com.ceiba.bl.parking.models.Bill;
import com.ceiba.bl.parking.models.Parking;
import com.ceiba.bl.parking.models.Vehicle;

public interface IParking {

	public Bill registerVehicle(String parkingId, Vehicle vehicle) throws Exception;	
	Bill charge(String parkingId, String licensePlate) throws Exception;
	public Parking registerParking (Parking parking) throws Exception ;
	Set<String> searchVehicles (String parkingId) throws Exception;
}
