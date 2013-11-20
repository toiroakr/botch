package com.example.mapdemo;

public class Restaurant {
	private int rst_id;
	private String restaurantName;
	private double difficalty;
	private String category;
	private double lon;
	private double lat;

	public Restaurant(int rst_id, String restaurantName, double diffcalty,
			String category) {
		this.rst_id = rst_id;
		this.restaurantName = restaurantName;
		this.difficalty = diffcalty;
		this.category = category;
	}

	public Restaurant(int rst_id, String restaurantName, double diffcalty,
			String category, double lon, double lat) {
		this.rst_id = rst_id;
		this.restaurantName = restaurantName;
		this.difficalty = diffcalty;
		this.category = category;
		this.lon = lon;
		this.lat = lat;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public double getDifficalty() {
		return difficalty;
	}

	public String getCategory() {
		return category;
	}

	public String toString() {
		return category + " - " + restaurantName;
	}

	public double getLon() {
		return lon;
	}

	public double getLat() {
		return lat;
	}

	public int getRst_id() {
		return rst_id;
	}
}