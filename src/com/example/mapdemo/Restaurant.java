package com.example.mapdemo;


public class Restaurant {
	int rst_id;
	String restaurantName;
	double difficalty;
	String category;

	public Restaurant(int rst_id, String restaurantName, double diffcalty, String category){
		this.rst_id = rst_id;
		this.restaurantName = restaurantName;
		this.difficalty = diffcalty;
		this.category = category;
	}



	public String getRestaurantName(){
		return restaurantName;
	}

	public double getDifficalty(){
		return difficalty;
	}

	public String getCategory(){
		return category;
	}

	public String toString(){
		return category + " - " + restaurantName;
	}
}