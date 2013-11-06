package com.example.mapdemo;

public class RestaurantDetail {
	private int rst_id;
	private String RestaurantName;
	private String TabelogMobileUrl;
	private double TotalScore;
	private String Situation;
	private String DinnerPrice;
	private String lunchprice;
	private String Category;
	private String Station;
	private String Address;
	private String Tel;
	private String Businesshours;
	private String Holiday;
	private double lat;
	private double lng;
	private double raw_difficulty;
	private double difficulty;


	public RestaurantDetail(int rst_id, String RestaurantName, String TabelogMobileUrl, double TotalScore, String Situation, 
			String DinnerPrice, String lunchprice, String Category, String Station, String Address, String Tel, String Businesshours, String Holiday,
			double lat, double lng, double difficulty, double raw_difficulty){
		this.setRst_id(rst_id);
		this.setRestaurantName(RestaurantName);
		this.setTabelogMobileUrl(TabelogMobileUrl);
		this.setTotalScore(TotalScore);
		this.setSituation(Situation);
		this.setDinnerPrice(DinnerPrice);
		this.setLunchprice(lunchprice);
		this.setCategory(Category);
		this.setStation(Station);
		this.setAddress(Address);
		this.setTel(Tel);
		this.setBusinesshours(Businesshours);
		this.setHoliday(Holiday);
		this.setDifficulty(difficulty);
		this.setRaw_difficulty(raw_difficulty);
	}


	public String toString(){
		return this.getCategory() + ":" + this.getRestaurantName() + "(" + this.getRst_id() + ")";
	}
	public int getRst_id() {
		return rst_id;
	}


	public void setRst_id(int rst_id) {
		this.rst_id = rst_id;
	}


	public String getTabelogMobileUrl() {
		return TabelogMobileUrl;
	}


	public void setTabelogMobileUrl(String TabelogMobileUrl) {
		this.TabelogMobileUrl = TabelogMobileUrl;
	}


	public double getTotalScore() {
		return TotalScore;
	}


	public void setTotalScore(double TotalScore) {
		this.TotalScore = TotalScore;
	}


	public String getSituation() {
		return Situation;
	}


	public void setSituation(String Situation) {
		this.Situation = Situation;
	}


	public String getCategory() {
		return Category;
	}


	public void setCategory(String Category) {
		this.Category = Category;
	}
	public String getDinnerPrice() {
		return DinnerPrice;
	}


	public void setDinnerPrice(String DinnerPrice) {
		this.DinnerPrice = DinnerPrice;
	}


	public String getLunchprice() {
		return lunchprice;
	}


	public void setLunchprice(String lunchprice) {
		this.lunchprice = lunchprice;
	}


	public String getStation() {
		return Station;
	}


	public void setStation(String Station) {
		this.Station = Station;
	}


	public String getAddress() {
		return Address;
	}


	public void setAddress(String Address) {
		this.Address = Address;
	}


	public String getTel() {
		return Tel;
	}


	public void setTel(String Tel) {
		this.Tel = Tel;
	}


	public String getBusinesshours() {
		return Businesshours;
	}


	public void setBusinesshours(String Businesshours) {
		this.Businesshours = Businesshours;
	}


	public String getHoliday() {
		return Holiday;
	}


	public void setHoliday(String Holiday) {
		this.Holiday = Holiday;
	}


	public double getLat() {
		return lat;
	}


	public void setLat(double lat) {
		this.lat = lat;
	}


	public double getLng() {
		return lng;
	}


	public void setLng(double lng) {
		this.lng = lng;
	}


	public double getRaw_difficulty() {
		return raw_difficulty;
	}


	public void setRaw_difficulty(double raw_difficulty) {
		this.raw_difficulty = raw_difficulty;
	}


	public double getDifficulty() {
		return difficulty;
	}


	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}


	public String getRestaurantName() {
		return RestaurantName;
	}


	public void setRestaurantName(String RestaurantName) {
		this.RestaurantName = RestaurantName;
	}
}