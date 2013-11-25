package com.example.mapdemo;

import android.location.Location;

public class DistanceCalculator {
	private static Double DEG_TO_RAD = Math.PI / 180;

	public static double distaceFromCoodinates(double lat1, double lon1,
			double lat2, double lon2) {
		// ラジアンに変換
		double a_lat = lat1 * DEG_TO_RAD;
		double a_lon = lon1 * DEG_TO_RAD;
		double b_lat = lat2 * DEG_TO_RAD;
		double b_lon = lon2 * DEG_TO_RAD;

		// 緯度の平均、緯度間の差、経度間の差
		double latave = (a_lat + b_lat) / 2;
		double latidiff = a_lat - b_lat;
		double longdiff = a_lon - b_lon;

		// 子午線曲率半径
		// 半径を6335439m、離心率を0.006694で設定してます
		double meridian = 6335439 / Math.sqrt(Math.pow(
				1 - 0.006694 * Math.sin(latave) * Math.sin(latave), 3));

		// 卯酉線曲率半径
		// 半径を6378137m、離心率を0.006694で設定してます
		double primevertical = 6378137 / Math.sqrt(1 - 0.006694
				* Math.sin(latave) * Math.sin(latave));

		// Hubenyの簡易式
		double x = meridian * latidiff;
		double y = primevertical * Math.cos(latave) * longdiff;

		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public static Double distaceFromCoodinates(String lat1, String lon1,
			String lat2, String lon2) {
		return distaceFromCoodinates(Double.parseDouble(lat1),
				Double.parseDouble(lon1), Double.parseDouble(lat2),
				Double.parseDouble(lon2));
	}

	public static double distace(Location myLocation, Restaurant rst) {
		double loc_lon = myLocation.getLongitude();
		double loc_lat = myLocation.getLatitude();
		double rst_lon = rst.getLon();
		double rst_lat = rst.getLat();
		return distaceFromCoodinates(loc_lat, loc_lon, rst_lat, rst_lon);
	}
}
