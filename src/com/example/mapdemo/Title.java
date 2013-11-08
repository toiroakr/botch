package com.example.mapdemo;

public class Title{
	String titleName;
	String condition;
	String rank;
	int imgId;
	boolean isGet;


	public Title(String titleName, String rank,String condition){
		this.titleName = titleName;
		this.rank = rank;
		this.condition = condition;
		this.imgId =  R.drawable.ic_launcher;
		this.isGet = false;

	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public boolean isGet() {
		return isGet;
	}

	public void setGet(boolean isGet) {
		this.isGet = isGet;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getTitleName(){
		return titleName;
	}

	public String getRank(){
		return rank;
	}

	public String toString(){
		return titleName;
	}
}