package com.example.home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

	protected static final int UPDATE_TODAY_WEATHER = 0;
	private static final int UPDATE_THE_DEVICE = 1;
	private static final int UPDATE_DEVICE = 1;
	private static final int UPDATE_CITY = 0;
	private static final int DELETE_DEVICE = 2;
	private static final int UPDATE_BLUETOOTH = 3;
	private static String str_delete;
	private static ImageButton bt1, bt2;
	private static TextView city, region, time, humidity, data, temperature, weather, wind;
	private static TextView allhome, livingroom, bedroom, kitchen, study, set, bluetooth;
	private static SharedPreferences prefs;
	private static String cityCode = "101010200";
	private static String province = "北京";
	public static int flag = 0;
	public static int count = 0;
	public TodayWeather todayweather;
	public Equipment equipment;
	// public static ArrayList<String> equipArray;
	private ArrayAdapter<String> adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initEquip();
		//
		if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
			Log.d("Weather", "网络OK");
			Toast.makeText(MainActivity.this, "网络OK!", Toast.LENGTH_LONG).show();
			// 调用
			// Log.d("myWeather_initcitycode", cityCode + "init");
			city.setText(province);
			queryWeatherCode(cityCode);
		} else {
			Log.d("Weather", "网络挂了");
			Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
		}
		//

	}

	public void initView() {
		// textview
		city = (TextView) findViewById(R.id.textView2);
		region = (TextView) findViewById(R.id.textView3);
		// time = (TextView) findViewById(R.id.textView4);
		humidity = (TextView) findViewById(R.id.textView5);
		// pm25 = (TextView) findViewById(R.id.textView6);
		// pollute = (TextView) findViewById(R.id.textView7);
		// data = (TextView) findViewById(R.id.textView8);
		// temperature = (TextView) findViewById(R.id.textView9);
		temperature = (TextView) findViewById(R.id.textView4);
		weather = (TextView) findViewById(R.id.textView6);
//		wind = (TextView) findViewById(R.id.textView12);
		allhome = (TextView) findViewById(R.id.textView7);
		allhome.setOnClickListener(this);
		livingroom = (TextView) findViewById(R.id.textView8);
		livingroom.setOnClickListener(this);
		bedroom = (TextView) findViewById(R.id.textView9);
		bedroom.setOnClickListener(this);
		kitchen = (TextView) findViewById(R.id.textView10);
		kitchen.setOnClickListener(this);
		study = (TextView) findViewById(R.id.textView11);
		study.setOnClickListener(this);
		set = (TextView) findViewById(R.id.textView13);
		set.setOnClickListener(this);
		bluetooth = (TextView) findViewById(R.id.textView16);
		bluetooth.setOnClickListener(this);
		bt1 = (ImageButton) findViewById(R.id.imButton1);
		bt1.setOnClickListener(this);
		bt2 = (ImageButton) findViewById(R.id.imButton2);
		bt2.setOnClickListener(this);
		prefs = getSharedPreferences("MyPref", MODE_PRIVATE);

	}

	public void initEquip() {
		todayweather = new TodayWeather();
		// equipArray = new ArrayList<String>();
		equipment = new Equipment();
		// equipment.addDevice("位置" + ":" + "名称" + " " + "状态" + " " + "功率");
		equipment.addDevice("bedroom" + ":" + "light1" + " " + "on" + " " + "50", ++count);
		equipment.addDevice("study" + ":" + "light2" + " " + "on" + " " + "100", ++count);
		equipment.addDevice("livingroom" + ":" + "light3" + " " + "on" + " " + "150", ++count);
		equipment.addDevice("livingroom" + ":" + "tv1" + " " + "on" + " " + "200", ++count);
		adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, equipment.device);
		ListView listview = (ListView) findViewById(R.id.listView1);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(MainActivity.this);
	}

	private void queryWeatherCode(String cityCode) {
		final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
		Log.d("myWeather", address);
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection con = null;
				try {
					URL url = new URL(address);
					con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					con.setConnectTimeout(8000);
					con.setReadTimeout(8000);
					InputStream in = con.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String str;
					while ((str = reader.readLine()) != null) {
						response.append(str);
						Log.d("myWeather_str", str);
					}
					String responseStr = response.toString();
					// Log.d("myWeather", responseStr);
					// Todo 解析返回的xml
					todayweather = parseXML(responseStr);
					parseXML(responseStr);
					if (todayweather != null) {
						Log.d("myWeather", todayweather.toString());
						Message msg = new Message();
						//
						//
						msg.what = UPDATE_TODAY_WEATHER;
						msg.obj = todayweather;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (con != null) {
						con.disconnect();
					}
					Log.d("myWeather", "Thread ending");
				}
			}
		}).start();
	}

	void updateTodayWeather(TodayWeather todayWeather) {
		region.setText(todayWeather.getRegion_today());
		// time.setText(todayWeather.getTime_today() + "发布");
		humidity.setText("湿度" + todayWeather.getHumidity_today());
		// wind.setText("风力" + todayWeather.getWind_today());
		// data.setText(todayWeather.getData_today());
		weather.setText(todayWeather.getWeather_today());
		temperature.setText(todayWeather.getTemperature_today() + "度");
	}

	private TodayWeather parseXML(String xmldata) {

		int fengxiangCount = 0;
		int fengliCount = 0;
		int dateCount = 0;
		int highCount = 0;
		int lowCount = 0;
		int typeCount = 0;
		try {
			XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = fac.newPullParser();
			xmlPullParser.setInput(new StringReader(xmldata));
			int eventType = xmlPullParser.getEventType();
			Log.d("myWeather", "parseXML");
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				// 判断当前事件是否为文档开始事件
				case XmlPullParser.START_DOCUMENT:
					break;
				// 判断当前事件是否为标签元素开始事件
				case XmlPullParser.START_TAG:
					if (xmlPullParser.getName().equals("city")) {
						eventType = xmlPullParser.next();
						todayweather.setRegion(xmlPullParser.getText());
						Log.d("myWeather", "city: " + xmlPullParser.getText());
					} else if (xmlPullParser.getName().equals("updatetime")) {
						eventType = xmlPullParser.next();
						Log.d("myWeather", "updatetime:" + xmlPullParser.getText());

					} else if (xmlPullParser.getName().equals("shidu")) {
						eventType = xmlPullParser.next();
						todayweather.setHumidity(xmlPullParser.getText());
						Log.d("myWeather", "shidu: " + xmlPullParser.getText());
					} else if (xmlPullParser.getName().equals("wendu")) {
						eventType = xmlPullParser.next();
						todayweather.setTemp(xmlPullParser.getText());
						Log.d("myWeather", "wendu: " + xmlPullParser.getText());
					} else if (xmlPullParser.getName().equals("pm25")) {
						eventType = xmlPullParser.next();
						// pm25.setText(xmlPullParser.getText());
						Log.d("myWeather", "pm25: " + xmlPullParser.getText());
					} else if (xmlPullParser.getName().equals("quality")) {
						eventType = xmlPullParser.next();
						Log.d("myWeather", "quality: " + xmlPullParser.getText());
					} else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
						eventType = xmlPullParser.next();
						Log.d("myWeather", "fengxiang:" + xmlPullParser.getText());
						fengxiangCount++;
					} else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
						eventType = xmlPullParser.next();
						// wind.setText("风力" + xmlPullParser.getText());
						// todayweather.setWind(xmlPullParser.getText());
						Log.d("myWeather", "fengli: " + xmlPullParser.getText());
						fengliCount++;
					} else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
						eventType = xmlPullParser.next();
						// data.setText(xmlPullParser.getText());
						// todayweather.setData(xmlPullParser.getText());
						Log.d("myWeather", "date: " + xmlPullParser.getText());
						dateCount++;
					} else if (xmlPullParser.getName().equals("high") && highCount == 0) {
						eventType = xmlPullParser.next();
						Log.d("myWeather", "high: " + xmlPullParser.getText());
						highCount++;
					} else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
						eventType = xmlPullParser.next();
						Log.d("myWeather", "low: " + xmlPullParser.getText());
						lowCount++;
					} else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
						eventType = xmlPullParser.next();
						// weather.setText(xmlPullParser.getText());
						todayweather.setWeather(xmlPullParser.getText());
						Log.d("myWeather", "type: " + xmlPullParser.getText());
						typeCount++;
					}
					break;
				// 判断当前事件是否为标签元素结束事件
				case XmlPullParser.END_TAG:
					break;
				}
				// 进入下一个元素并触发相应事件
				eventType = xmlPullParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return todayweather;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.textView16:// 蓝牙连接
			Intent p = new Intent(this, Bluetooth.class);
			Log.d("bluettoth fir", "ok");
			startActivityForResult(p, UPDATE_BLUETOOTH);
			break;
		case R.id.textView13:// 添加设备
			Intent i = new Intent(this, AddDelete.class);
			startActivityForResult(i, UPDATE_DEVICE);
			break;

		case R.id.imButton2:// 刷新
			if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
				Log.d("myWeather", "网络OK");
				city.setText(province);// 省份
				queryWeatherCode(cityCode);
			} else {
				Log.d("myWeather", "网络挂了");
				// mToast.makeText(this, "网络挂了!", Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.imButton1:// 选择城市
			Intent j = new Intent(this, SelectCity.class);
			startActivityForResult(j, UPDATE_CITY);
			break;
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		str_delete = equipment.device.get(position).toString();
		Log.d("str_delete", str_delete);
		Intent k = new Intent(this, Delete.class);
		startActivityForResult(k, DELETE_DEVICE);
	}

	protected void onResume() {
		super.onResume();
//		// 取得偏好设置数据
//		SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		if (flag == 0) {
			cityCode = prefs.getString("MAIN_CITY_CODE", "101010200");
			province = prefs.getString("MAIN_CITY_PROVINCE", "北京");
			Log.d("myWeather", cityCode);
			Log.d("myWeather", province);
			// debug调试
			city.setText(province);// 省份
			queryWeatherCode(cityCode);
		}
		flag = 0;
	}

	protected void onPause() {
		super.onPause();
		// 取得Editor对象
		SharedPreferences.Editor prefEdit = prefs.edit();
//		// 存入偏好设置数据到Editor对象
		Log.d("myWeather_cuncitycode", cityCode);
		prefEdit.putString("MAIN_CITY_PROVINCE", province);
		prefEdit.putString("MAIN_CITY_CODE", cityCode);
		prefEdit.commit(); // 确认写入文件
		flag = 1;

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("onactivity", "ok");
		if (requestCode == UPDATE_DEVICE) {
			String device;
			device = data.getStringExtra("device");
			Log.d("adddevice", device);
			if (!device.equals("nodevice")) {
				Log.d("adddevice in", device);

				// equipArray.add(device);
				equipment.addDevice(device, ++count);
				write(device, count);
				Log.d("mydevice", "addsuccess");
			} else if (device.equals("nodevice")) {
				Log.d("adddevice in", "nodevice");
			}
			Message msg = new Message();
			msg.what = UPDATE_THE_DEVICE;
			// msg.obj = equipArray;
			msg.obj = equipment;
			mHandler.sendMessage(msg);
		} else if (requestCode == UPDATE_CITY) {
			cityCode = data.getStringExtra("cityCode");
			province = data.getStringExtra("province");
			// String province = data.getStringExtra("province");
			if (cityCode != null) {
				Log.d("myWeather_zaiciqidong", cityCode);
				city.setText(province);// 省份
				queryWeatherCode(cityCode);
			}
		} else if (requestCode == DELETE_DEVICE) {

			String getDelete = data.getStringExtra("devicedelete");
			Log.d("delete device", getDelete);
			if (!getDelete.equals("nodevice")) {
				if (!getDelete.equals("delete")) {
					String newdevice = str_delete.split(" ")[0] + " " + getDelete + "W";
					equipment.changeDevice(newdevice, str_delete);
					Toast.makeText(MainActivity.this, Bluetooth.mThread.isAlive() + " ", Toast.LENGTH_LONG).show();
					write(newdevice, equipment.map.get(str_delete.split(" ")[0].split(":")[1]));
				} else
					equipment.deleteDevice(str_delete);
			}
			Message msg = new Message();
			msg.what = UPDATE_THE_DEVICE;
			// msg.obj = equipArray;
			msg.obj = equipment;
			mHandler.sendMessage(msg);
		} else if (requestCode == UPDATE_BLUETOOTH) {
			String sendinit = "03 02", send = null;
			for (int i = 0; i < equipment.device.size(); i++)
				write(equipment.device.get(i), i + 1);
		}
	}

	private void write(String str, int i) {
		String sendinit = "03 02", send = null;
		if (str.split(":")[1].split(" ")[1].equals("on"))
			send = sendinit + " " + "0" + Integer.toString(i) + " " + "01";// 开(i+1单位数)
		else
			send = sendinit + " " + "0" + Integer.toString(i) + " " + "00";// 关
		Bluetooth.mThread.write(send.getBytes());
		try {
			Thread.sleep(300);// 使数据流传送完毕

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Toast.makeText(MainActivity.this, Bluetooth.result, Toast.LENGTH_LONG).show();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_TODAY_WEATHER:
				Log.d("Weather", "begin_update");
				updateTodayWeather((TodayWeather) msg.obj);
				break;
			case UPDATE_THE_DEVICE:
				Log.d("Device_handler", "begin_update");// 更新item

				// adapter = new ArrayAdapter<String>(MainActivity.this,
				// android.R.layout.simple_list_item_1, equipArray);
				adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,
						equipment.device);
				ListView listview = (ListView) findViewById(R.id.listView1);
				listview.setAdapter(adapter);
				listview.setOnItemClickListener(MainActivity.this);
				break;
			default:
				break;
			}
		}

	};

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
