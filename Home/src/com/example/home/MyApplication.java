package com.example.home;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

public class MyApplication extends Application {
	private static final String TAG = "MyApp";
	private static MyApplication mApplication;
	private CityDB mCityDB;
	private List<City> mCityList;

	public static MyApplication getInstance() {
		return mApplication;
	}

	public List<City> getCityList() {
		return mCityList;
	}

	public void onCreate() {
		Log.d(TAG, "MyApplication->onCreate");
		super.onCreate();
		// Log.d(TAG, "MyApplication->onCreate");
		mApplication = this;
		mCityDB = openCityDB();
		initCityList();
	}

	private CityDB openCityDB() {
		String path = "/data" + Environment.getDataDirectory().getAbsolutePath() + File.separator + getPackageName()
				+ File.separator + "database1" + File.separator + CityDB.CITY_DB_NAME;
		File db = new File(path);
		Log.d(TAG, path);
		if (!db.exists()) {
			String pathfolder = "/data" + Environment.getDataDirectory().getAbsolutePath() + File.separator
					+ getPackageName() + File.separator + "database1" + File.separator;
			File dirFirstFolder = new File(pathfolder);
			if (!dirFirstFolder.exists()) {
				dirFirstFolder.mkdirs();
				Log.d("MyApp", "mkdirs");

			}
			Log.d("MyApp", "db is not exist");
			try {
				InputStream is = getAssets().open("city.db");
				FileOutputStream fos = new FileOutputStream(db);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				fos.close();
				is.close();

			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return new CityDB(this, path);
	}

	private void initCityList() {
		// TODO Auto-generated method stub
		mCityList = new ArrayList<City>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				prepareCityList();
			}

		}).start();
	}

	private boolean prepareCityList() {
		mCityList = mCityDB.getAllCity();
		int i = 0;
		for (City city : mCityList) {
			i++;
			String cityName = city.getCity();
			String cityCode = city.getNumber();

		}
		Log.d(TAG, "i=" + i);
		return true;

	}

}
