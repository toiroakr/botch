package com.example.mapdemo;


public class Title{
	int id;
	String titleName;
	String condition;
	String rank;
	String imgStr;
	boolean isGet;


	public Title(int id,String titleName, String rank,String condition){
		this.id = id;
		this.titleName = titleName;
		this.rank = rank;
		this.condition = condition;
		this.imgStr = "hatena";
		this.isGet = false;

	}

	public Title(){
		this.id = -1;
		this.titleName = null;
		this.rank = null;
		this.condition = null;
		this.imgStr = null;
		this.isGet = false;
	}

	public Title(int id,String titleName, String rank,String condition,String ImgStr){
		this.id = id;
		this.titleName = titleName;
		this.rank = rank;
		this.condition = condition;
		this.imgStr = ImgStr;
		this.isGet = false;

	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getImgStr() {
		return imgStr;
	}

	public void setImgId(String imgStr) {
		this.imgStr = imgStr;
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