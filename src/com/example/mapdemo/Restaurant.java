package com.example.mapdemo;

public class Restaurant {
	private int rst_id;
	private String restaurantName;
	private double raw_difficulty;
	private int difficulty;
	private String category;
	private double lon;
	private double lat;

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass())
			return false;
		Restaurant rst = (Restaurant) obj;
		if (rst_id == rst.getRst_id())
			return true;
		return false;
	}

	public Restaurant(int rst_id, String restaurantName, double raw_difficulty,
			int difficulty, String category) {
		this.rst_id = rst_id;
		this.restaurantName = restaurantName;
		this.difficulty = difficulty;
		this.setRaw_difficulty(raw_difficulty);
		this.category = category;
	}

	public Restaurant(int rst_id, String restaurantName, double raw_difficulty,
			int difficulty, String category, double lon, double lat) {
		this.rst_id = rst_id;
		this.restaurantName = restaurantName;
		this.difficulty = difficulty;
		this.setRaw_difficulty(raw_difficulty);
		this.category = category;
		this.lon = lon;
		this.lat = lat;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public double getDifficulty() {
		return difficulty;
	}

	public String getCategory() {
		return category;
	}

	public String toString() {
		return category + " - " + restaurantName + "(" + lon + ", " + lat + ")";
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

	public double getRaw_difficulty() {
		return raw_difficulty;
	}

	public void setRaw_difficulty(double raw_difficulty) {
		this.raw_difficulty = raw_difficulty;
	}
}