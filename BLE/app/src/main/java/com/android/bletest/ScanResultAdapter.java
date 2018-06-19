package com.android.bletest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ScanResultAdapter extends BaseAdapter {
	private final static String TAG = "ScanResultAdapter";

	private ArrayList<BLEDevice> devices = new ArrayList<BLEDevice>();
	private Context context;

	public ScanResultAdapter(Context context, ArrayList<BLEDevice> devices){
		this.context = context;
		this.devices = devices;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position < 0){
			Log.e(TAG,"连接设备的ViewPagerAdapter的getItem出现问题，position = " + position);
			return devices.get(0);
		}
		if(position > devices.size() -1){
			Log.e(TAG,"连接设备的ViewPagerAdapter的getItem出现问题，position = "+position);
			return devices.get(devices.size()-1);
		}
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
		// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_connect_device, null);
			holder.device_name = (TextView) convertView.findViewById(R.id.tv_item_connect_device_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final String deviceName = devices.get(position).getBluetoothDevice().getName();
		final String address = devices.get(position).getBluetoothDevice().getAddress();
		final int rssi = devices.get(position).getRssi();


		if (deviceName != null && deviceName.length() > 0) {
			holder.device_name.setText(deviceName+"         "+rssi+"\n"+""+address);
//			holder.device_name.setText(deviceName+"\n"+""+address);
		} else {
			holder.device_name.setText("unknow");
		}

		return convertView;
	}

	public void refreshData(ArrayList<BLEDevice> devices){
		this.devices = devices;
		notifyDataSetChanged();
	}




	private class ViewHolder {

		TextView device_name;

	}

}
