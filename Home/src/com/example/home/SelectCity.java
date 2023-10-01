package com.example.home;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectCity extends Activity implements OnItemClickListener {

	private ArrayAdapter<String> adapter;
	private ArrayList<String> mArrayList;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		String cityCode = mArrayList.get(position).split(" ")[1];
		Log.d("weather_sec", cityCode);
		String province = mArrayList.get(position).split(" ")[2];
		Log.d("weather_sec", province);
		Intent intent = new Intent();
		intent.putExtra("cityCode", cityCode);
		intent.putExtra("province", province);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_city);

		mArrayList = new ArrayList<String>();
		MyApplication myApplication = (MyApplication) getApplication();
		List<City> cityList = new ArrayList<City>();
		cityList = myApplication.getCityList();
		for (int i = 0; i < cityList.size(); i++) {
			String NO_ = Integer.toString(i + 1);
			String number = cityList.get(i).getNumber();
			String province = cityList.get(i).getProvince();
			String cityname = cityList.get(i).getCity();
			mArrayList.add("No:" + NO_ + " " + number + " " + province + " " + cityname);
		}

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList);
		ListView mList = (ListView) findViewById(R.id.listView1);
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_city, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
