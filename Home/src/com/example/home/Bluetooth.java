package com.example.home;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Bluetooth extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

	private BluetoothAdapter bluetoothAdapter;
	public BluetoothSocket mSocket;
	private BroadcastReceiver receiver;

	private ListView listView;
	private ArrayAdapter<String> deviceAdapter;
	private List<String> listDevices;

	private TextView textData;

	private Button btnOpen;
	private Button btnSearch;

	private EditText editSend;
	private Button btnSend;

	private TextView textStatus;

	public static ComThread mThread = null;
	// private ConnectThread mThread = null;
	public static String result = "";
	private final int CONNECTED = 1000;
	private final int DISCONNECTED = 1001;
	private int state = DISCONNECTED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);

		listView = (ListView) this.findViewById(R.id.listView);
		listDevices = new ArrayList<String>();
		deviceAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,
				listDevices);
		listView.setAdapter(deviceAdapter);
		listView.setOnItemClickListener(this);// ��Ӽ���

		textData = (TextView) this.findViewById(R.id.text);
		// textData.setText(textData.getText(),
		// TextView.BufferType.EDITABLE);//���п�ʵ��TextViewβ��׷��

		editSend = (EditText) findViewById(R.id.editSend);
		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(this);

		btnOpen = (Button) findViewById(R.id.btnOpen);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnOpen.setOnClickListener(this);
		btnSearch.setOnClickListener(this);

		textStatus = (TextView) findViewById(R.id.textStatus);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter.isEnabled()) {
			btnOpen.setText("�ر�����");
		}

		if (Build.VERSION.SDK_INT >= 23) {
			boolean isLocat = isLocationOpen(getApplicationContext());
			Toast.makeText(getApplicationContext(), "isLo:" + isLocat, Toast.LENGTH_SHORT).show();
			// ����λ�÷���֧�ֻ�ȡble����ɨ����
			if (!isLocat) {
				Intent enableLocate = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(enableLocate, 1);
			}
		}

		// �����㲥����
		receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// �����豸ʱ��ȡ���豸��MAC��ַ
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					String str = device.getName() + "|" + device.getAddress();
					if (listDevices.indexOf(str) == -1)// ��ֹ�ظ����
						listDevices.add(str); // ��ȡ�豸���ƺ�mac��ַ
					if (deviceAdapter != null) {
						deviceAdapter.notifyDataSetChanged();
					}
				}
			}
		};

	}

	// �ж�λ����Ϣ�Ƿ���
	private static boolean isLocationOpen(final Context context) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// gps��λ
		boolean isGpsProvider = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// ���綨λ
		boolean isNetWorkProvider = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return isGpsProvider || isNetWorkProvider;
	}

	@Override
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

//	@Override
//	protected void onDestroy() {
//		if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
//			bluetoothAdapter.cancelDiscovery();
//		}
//		unregisterReceiver(receiver);
//		if (mSocket != null) {
//			try {
//				mSocket.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		super.onDestroy();
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOpen:
			if (!bluetoothAdapter.isEnabled()) {
				bluetoothAdapter.enable();// ��������
				Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				enable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); // 300��Ϊ�����豸�ɼ�ʱ��
				startActivity(enable);
				btnOpen.setText("�ر�����");
			} else {
				bluetoothAdapter.disable();// �ر�����
				btnOpen.setText("��������");
				if (mSocket != null) {
					try {
						mSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			break;
		case R.id.btnSearch:
			if (!bluetoothAdapter.isEnabled()) {
				Toast.makeText(getApplicationContext(), "���ȿ�������", Toast.LENGTH_SHORT).show();
			} else {
				// btContent.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				if (listDevices != null) {
					listDevices.clear();
					if (deviceAdapter != null) {
						deviceAdapter.notifyDataSetChanged();
					}
				}

				bluetoothAdapter.startDiscovery();
				Toast.makeText(getApplicationContext(), "��ʼ����", Toast.LENGTH_SHORT).show();
				IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
				registerReceiver(receiver, filter); // ע��㲥������
			}
			break;
		case R.id.btnSend:
			if (state == CONNECTED && mThread != null) {
				String info = editSend.getText().toString();

				textStatus.setText("Send:" + info);
				textData.setText("Send Data:" + info + "\r\n");
				// textData.append("Send Data:"+info+"\r\n");
				mThread.write(info.getBytes());
			}
			break;

		}

	}

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

		if (!bluetoothAdapter.isEnabled()) {
			Toast.makeText(getApplicationContext(), "���ȿ�������", Toast.LENGTH_SHORT).show();
		} else {
			bluetoothAdapter.cancelDiscovery();// ֹͣ����

			String str = listDevices.get(position);
			String macAdress = str.split("\\|")[1];

			BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAdress);
			try {

				/*
				 * final String SPP_UUID=""; UUID uuid = UUID.fromString(SPP_UUID); //Standard
				 * SerialPortService ID mSocket =
				 * device.createRfcommSocketToServiceRecord(uuid);
				 */

				Method clientMethod = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
				mSocket = (BluetoothSocket) clientMethod.invoke(device, 1);
				// 1 ���ӵ�Ƭ�� , 2��3 �����ֻ�

				try {
					mSocket.connect();// ����
					textStatus.setText("��������..........");
					if (mSocket.isConnected()) {
						textStatus.setText("���ӳɹ�");
						Toast.makeText(getApplicationContext(), "�������ӳɹ�", Toast.LENGTH_SHORT).show();
						listView.setVisibility(View.GONE);
						textData.setVisibility(View.VISIBLE);
						mThread = new ComThread(mSocket);
						mThread.start();// ��һ���̣߳��������豸����ͨ��
						state = CONNECTED;
						Intent blue = new Intent();
						setResult(RESULT_OK, blue);
						finish();
					} else {
						textStatus.setText("����ʧ��");
						Toast.makeText(getApplicationContext(), "��������ʧ��", Toast.LENGTH_SHORT).show();
						mSocket.close();
						listView.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	private void setResult(String string, Intent blue) {
		// TODO Auto-generated method stub

	}

	public static final int MESSAGE_READ = 0;
	public static final int DEBUG = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_READ:
				// textData.append("Receive: "+(String)msg.obj+"\r\n");
				textData.setText("Receive: " + (String) msg.obj + "\r\n");
				break;
			case DEBUG:
				textStatus.setText((String) msg.obj);
				break;
			default:
				break;
			}
		}

	};

	class ComThread extends Thread {

		private BluetoothSocket s;
		private boolean exitflag = false;

		public ComThread(BluetoothSocket s) {
			this.s = s;
		}

		public synchronized boolean getFlag() {
			return exitflag;
		}

		public synchronized void setFlag(boolean v) {
			exitflag = true;
		}

		private InputStream inputStream;
		private OutputStream outputStream;

		public void write(byte[] bytes) {
			try {
				outputStream = mSocket.getOutputStream();
				outputStream.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				inputStream = s.getInputStream();

				int len = 0;
				// String result = "";

				exitflag = false;
				while (len != -1) {
					if (inputStream.available() <= 0) {
						Thread.sleep(1000);// �ȴ�0.5�룬�����ݽ�������
						continue;
					} else {
						try {
							Thread.sleep(500);// �ȴ�0.5�룬�����ݽ�������
							byte[] data = new byte[1024];
							len = inputStream.read(data);
							result = URLDecoder.decode(new String(data, "utf-8"));

							/*
							 * String debuginfo="len = "+len+" result = "+result; Message msg1 = new
							 * Message(); msg1.what = DEBUG; msg1.obj = debuginfo;
							 * mHandler.sendMessage(msg1);
							 * 
							 * 
							 * Message msg = new Message(); msg.what = MESSAGE_READ; msg.obj = result;
							 * mHandler.sendMessage(msg);
							 */

							final String mid = result;
							textData.post(new Runnable() {
								public void run() {
									// textData.append("Receiv: "+mid);
									textData.setText("Receiv: " + mid);
									// String oldstr = textData.getText().toString();
									// textData.setText(oldstr+"\r\nReceiv: "+mid);
								}
							});
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				inputStream.close();
				outputStream.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
