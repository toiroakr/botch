package com.example.mapdemo;

public class Restaurant {
  String restaurantName;
  double difficalty;
  String category;

  public Restaurant(String restaurantName, double diffcalty, String category){
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