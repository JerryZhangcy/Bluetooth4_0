package com.android.bletest;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class CharacteristicAdapter extends BaseAdapter {
    private final static String TAG = "CharacteristicAdapter";

    List<BluetoothGattCharacteristic> characteristicList;
    private Context context;

    public CharacteristicAdapter(Context context, List<BluetoothGattCharacteristic> characteristicList) {
        this.context = context;
        this.characteristicList = characteristicList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return characteristicList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (position < 0) {
            return characteristicList.get(0);
        }
        if (position > characteristicList.size() - 1) {
            return characteristicList.get(characteristicList.size() - 1);
        }
        return characteristicList.get(position);
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
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int charaProp = characteristicList.get(position).getProperties();
        StringBuffer deviceName = new StringBuffer();
        deviceName.append("属性：");
        // 可读
        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            deviceName.append("   可读");
        }
       // 可写，注：要 & 其可写的两个属性
        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0
                || (charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
            deviceName.append("   可写");
        }
       // 可通知，可指示
        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0
                || (charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
            deviceName.append("   可通知");
        }

        String uuid = characteristicList.get(position).getUuid().toString();
        holder.tv_item_servicesname.setText(deviceName.toString());
        holder.tv_item_uuid.setText(uuid);

        return convertView;
    }

    public void refreshData(List<BluetoothGattCharacteristic> characteristicList) {
        this.characteristicList = characteristicList;
        notifyDataSetChanged();
    }


    private class ViewHolder {

        TextView tv_item_servicesname;
        TextView tv_item_uuid;

    }

}
