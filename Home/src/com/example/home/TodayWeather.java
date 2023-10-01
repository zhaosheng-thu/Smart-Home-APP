package com.example.home;

public class TodayWeather {
	private String city_today, region_today, time_today, humidity_today, data_today, temperature_today, weather_today,
			wind_today;

	TodayWeather() {
		city_today = "N/A";
		region_today = "N/A";
		time_today = "N/A";
		humidity_today = "N/A";
		data_today = "N/A";
		temperature_today = "N/A";
		weather_today = "N/A";
		wind_today = "N/A";
	}

	public String getCity_today() {
		return city_today;
	}

	public String getRegion_today() {
		return region_today;
	}

	public String getTime_today() {
		return time_today;
	}

	public String getHumidity_today() {
		return humidity_today;
	}

	public String getData_today() {
		return data_today;
	}

	public String getTemperature_today() {
		return temperature_today;
	}

	public String getWeather_today() {
		return weather_today;
	}

	public String getWind_today() {
		return wind_today;
	}

	//
	public void setCity(String str) {
		city_today = str;
	}

	public void setRegion(String str) {
		region_today = str;
	}

	public void setTime(String str) {
		time_today = str;
	}

	public void setHumidity(String str) {
		humidity_today = str;
	}

	public void setData(String str) {
		data_today = str;
	}

	public void setTemp(String str) {
		temperature_today = str;
	}

	public void setWeather(String str) {
		weather_today = str;
	}

	public void setWind(String str) {
		wind_today = str;
	}

}