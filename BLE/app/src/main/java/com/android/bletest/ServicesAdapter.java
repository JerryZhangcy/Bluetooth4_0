package com.android.bletest;

import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ServicesAdapter extends BaseAdapter {
	private final static String TAG = "ServicesAdapter";

	List<BluetoothGattService> servicesList;
	private Context context;

	public ServicesAdapter(Context context,List<BluetoothGattService> servicesList){
		this.context = context;
		this.servicesList = servicesList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return servicesList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position < 0){
			return servicesList.get(0);
		}
		if(position > servicesList.size() -1){
			return servicesList.get(servicesList.size()-1);
		}
		return servicesList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_services, null);
			holder.tv_item_servicesname = (TextView) convertView.findViewById(R.id.tv_item_servicesname);
			holder.tv_item_uuid = (TextView) convertView.findViewById(R.id.tv_item_uuid);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		String deviceName = servicesList.get(position).toString();
		String uuid = servicesList.get(position).getUuid().toString();
		holder.tv_item_servicesname.setText(deviceName);
		holder.tv_item_uuid.setText(uuid);

		return convertView;
	}

	public void refreshData(List<BluetoothGattService> servicesList){
		this.servicesList = servicesList;
		notifyDataSetChanged();
	}




	private class ViewHolder {

		TextView tv_item_servicesname;
		TextView tv_item_uuid;

	}

}
