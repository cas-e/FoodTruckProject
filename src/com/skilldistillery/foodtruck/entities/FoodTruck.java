package com.skilldistillery.foodtruck.entities;

public class FoodTruck {
	private String name;
	private String type;
	private int rating;
	private int id;

	// used to generate unique values
	private static int nextTruckId;

	public FoodTruck(String n, String t, int r) {
		name = n;
		type = t;
		rating = r;
		id = nextTruckId;
		nextTruckId++;
	}
	
	public int getRating() {
		return rating;
	}

	@Override
	public String toString() {

		// EZ data alignment: put all constant length data on the left of the lines,
		// and all variable length data on the right. 
		String line1 = "Truck ID: " + id + " | Name: " + name + "\n";
		String line2 = "Rating:   " + rating + " | Type: " + type + "\n"; 
		return line1 + line2;
	}

}
