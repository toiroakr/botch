package com.example.mapdemo;

public class Title{
  String titleName;
  String rank;

  public Title(String titleName, String rank){
    this.titleName = titleName;
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