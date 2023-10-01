package com.example.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class City {

	public City(String province, String city, String number, String firstPY, String allPY, String allFirstPY) {
		// super();
		this.province = province;
		this.city = city;
		this.number = number;
		this.firstPY = firstPY;
		this.allPY = allPY;
		this.allFirstPY = allFirstPY;
	}

	private String province;
	private String city;
	private String number;
	private String firstPY;
	private String allPY;
	private String allFirstPY;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFirstPY() {
		return firstPY;
	}

	public void setFirstPY(String firstPY) {
		this.firstPY = firstPY;
	}

	public String getAllPY() {
		return allPY;
	}

	public void setAllPY(String allPY) {
		this.allPY = allPY;
	}

	public String getAllFirstPY() {
		return allFirstPY;
	}

	public void setAllFirstPY(String allFirstPY) {
		this.allFirstPY = allFirstPY;
	}

}

class CityDB {
	public static final String CITY_DB_NAME = "city.db";
	private static final String CITY_TABLE_NAME = "city";
	private SQLiteDatabase db;

	public CityDB(Context context, String path) {
		db = context.openOrCreateDatabase(path, context.MODE_PRIVATE, null);
	}

	public List<City> getAllCity() {
		List<City> list = new ArrayList<City>();
		Cursor c = db.rawQuery("SELECT * FROM " + CITY_TABLE_NAME, null);
		while (c.moveToNext()) {
			String province = c.getString(c.getColumnIndex("province"));
			String city = c.getString(c.getColumnIndex("city"));
			String number = c.getString(c.getColumnIndex("number"));
			String allPY = c.getString(c.getColumnIndex("allpy"));
			String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
			String firstPY = c.getString(c.getColumnIndex("firstpy"));
			City item = new City(province, city, number, firstPY, allPY, allFirstPY);
			list.add(item);
		}
		return list;
	}

}