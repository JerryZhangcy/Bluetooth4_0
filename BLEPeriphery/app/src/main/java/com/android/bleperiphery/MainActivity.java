package com.android.bleperiphery;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Nearby Device");

        if (savedInstanceState == null) {
            mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
            if (mBluetoothAdapter != null) {
                if (mBluetoothAdapter.isEnabled()) {
                    if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {
                        setupFragment();
                    } else {
                        Toast.makeText(MainActivity.this, "该设备不支持蓝牙广播", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent enableBleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBleIntent, Constants.REQUEST_ENABLE_BT);
                }
            } else {
                Toast.makeText(MainActivity.this, "不支持蓝牙", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {
                        setupFragment();
                    } else {
                        Toast.makeText(MainActivity.this, "该设备不支持蓝牙广播", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "不打开蓝牙设备", Toast.LENGTH_SHORT).show();
                    finish();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AdvertiserFragment advertiserFragment = new AdvertiserFragment();
        transaction.replace(R.id.advertiser_fragment_container, advertiserFragment);

        transaction.commit();
    }
}
