package com.android.bleperiphery;

import android.bluetooth.le.AdvertiseCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import static com.android.bleperiphery.BluetoothApplication.mAppContext;

/**
 * Created by lihong on 2017/10/24.
 */

public class AdvertiserFragment extends Fragment implements OnClickListener {

    private Switch mSwitch;
    private EditText mEditText;
    private MyBroadcastReceiver mMyBroadcastReceiver = new MyBroadcastReceiver();
    private String mContent = "";
    private int mCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advertiser, container, false);

        mSwitch = (Switch) view.findViewById(R.id.advertise_switch);
        mSwitch.setOnClickListener(this);
        mEditText = view.findViewById(R.id.msg_content);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        regBroadcastReceiver();
        if (HelmetBluetoothService.mServiceRunning) {
            mSwitch.setChecked(true);
        } else {
            mSwitch.setChecked(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegBroadcastReceiver();
    }

    private static Intent getServiceIntent(Context c) {
        return new Intent(c, HelmetBluetoothService.class);
    }

    @Override
    public void onClick(View v) {
        boolean on = ((Switch) v).isChecked();
        if (on) {
            startAdvertising();
        } else {
            stopAdvertising();
        }
    }

    private void startAdvertising() {
        Context c = getActivity();
        c.startService(getServiceIntent(c));
    }

    private void stopAdvertising() {
        Context c = getActivity();
        c.stopService(getServiceIntent(c));
        mSwitch.setChecked(false);
    }

    private void regBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(HelmetBluetoothService.ACTION);
        mAppContext.registerReceiver(mMyBroadcastReceiver, filter);
    }

    private void unRegBroadcastReceiver() {
        mAppContext.unregisterReceiver(mMyBroadcastReceiver);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(HelmetBluetoothService.ACTION)) {
                mCount++;
                if (mCount > 5) {
                    mCount = 0;
                    mContent = mCount + ":" + intent.getStringExtra(HelmetBluetoothService.DATA_KEY) + "\n";
                } else {
                    mContent += mCount + ":" + intent.getStringExtra(HelmetBluetoothService.DATA_KEY) + "\n";
                }
                mEditText.setText(mContent);
            }
        }
    }
}
