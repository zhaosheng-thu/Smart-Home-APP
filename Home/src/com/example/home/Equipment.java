package com.example.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

public class Equipment {
	ArrayList<String> device = new ArrayList<String>();
	Map<String, Integer> map;

	Equipment() {
		device = new ArrayList<String>();
		map = new HashMap<String, Integer>();
	}

	public ArrayList<String> getDevice() {
		return device;
	}

	public void setDevice(ArrayList<String> device) {
		this.device = device;
	}

	public void addDevice(String str, int num) {
		Log.d("equipment", str);
		device.add(str);
		map.put(str.split(" ")[0].split(":")[1], num);
	}

	public void deleteDevice(String str) {
		Iterator<String> iterator = device.iterator();
		while (iterator.hasNext()) {
			String s = iterator.next();
			if (s.equals(str))
				iterator.remove();
		}
	}

	public void changeDevice(String str, String old) {// 只改变信息(不改名，不改hashmap对应编码)
		this.deleteDevice(old);
		device.add(str);
	}
}