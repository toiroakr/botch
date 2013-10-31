package com.example.mapdemo;

public class User {
	private int id;
	private String user_name;
	private String home_place;
	public User(int num, String name, String place) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.setId(num);
		this.setUser_name(name);
		this.setHome_place(place);		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getHome_place() {
		return home_place;
	}
	public void setHome_place(String home_place) {
		this.home_place = home_place;
	}

}
