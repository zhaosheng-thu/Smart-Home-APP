package com.example.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddDelete extends Activity implements OnClickListener {

	private EditText txt_1, txt_2, txt_3, txt_4;
	private Button bt1, bt2;

	// String deviceName,deviceState,deviceNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_delete);
		txt_1 = (EditText) findViewById(R.id.editText1);
		txt_2 = (EditText) findViewById(R.id.editText2);
		txt_3 = (EditText) findViewById(R.id.editText3);
		txt_4 = (EditText) findViewById(R.id.editText4);
		bt1 = (Button) findViewById(R.id.button1);
		bt1.setOnClickListener(this);
		bt2 = (Button) findViewById(R.id.button2);
		bt2.setOnClickListener(this);
		Log.d("add and delete", "begin");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_delete, menu);
		return true;
	}

//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		
//	}
//	
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Intent intent = new Intent();
			String information;
			information = txt_4.getText().toString() + ":" + txt_1.getText().toString() + " "
					+ txt_2.getText().toString() + " " + txt_3.getText().toString() + "W";
			Log.d("adddelete", txt_1.getText().toString() + txt_2.getText().toString());
			intent.putExtra("device", information);
			// Log.d("adddelete",txt_1.getText().toString() + txt_2.getText().toString() );
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.button2:
			Intent intent_sec = new Intent();
			intent_sec.putExtra("device", "nodevice");
			setResult(RESULT_OK, intent_sec);
			finish();
			break;
		}
	}
}
