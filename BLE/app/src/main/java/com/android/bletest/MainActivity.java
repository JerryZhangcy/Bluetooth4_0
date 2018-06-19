package com.android.bletest;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_FINE_LOCATION = 0;

    Button main_btn_startorstop;
    Button main_btn_disconnect;
    Button main_btn_services;
    Button main_btn_write;

    ListView main_list_scanresule;
    Handler myhandler = new Handler();
    BleHelper mBle;
    boolean mScanning = false;
    ArrayList<BLEDevice> devicelist = new ArrayList<BLEDevice>();
    ScanResultAdapter adapter;

    BLEDevice currDevice;


    private static final long SCAN_PERIOD = 30000;

    private Runnable stopRunnable = new Runnable() {
        @Override
        public void run() {
            mBle.stopScan();
            mScanning = false;
            main_btn_startorstop.setText("扫描");

        }
    };

    private Runnable refreshAdapter = new Runnable() {
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
        }
    };

    private Runnable showbtn = new Runnable() {
        @Override
        public void run() {
            if (currDevice.isConntect()) {
                main_btn_disconnect.setVisibility(View.VISIBLE);
                main_btn_services.setVisibility(View.VISIBLE);
                main_btn_write.setVisibility(View.VISIBLE);

            } else {
                main_btn_disconnect.setVisibility(View.GONE);
                main_btn_services.setVisibility(View.GONE);
                main_btn_write.setVisibility(View.GONE);
            }
        }
    };

    private BleReadOrWriteCallback bleReadOrWriteCallback = new BleReadOrWriteCallback() {
        @Override
        public void onReadSuccess() {

        }

        @Override
        public void onReadFail(int errorCode) {

        }

        @Override
        public void onWriteSuccess() {

        }

        @Override
        public void onWriteFail(int errorCode) {

        }

        @Override
        public void onServicesDiscovered(int state) {

        }

        @Override
        public void onDiscoverServicesFail(int errorCode) {

        }
    };

    private BleConnectionCallback connectionCallback = new BleConnectionCallback() {
        @Override
        public void onConnectionStateChange(int status, int newState) {
            Log.e(TAG, "连接状态发生变化：" + status + "     " + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                currDevice.setConntect(true);
                mBle.discoverServices(currDevice.getBluetoothDevice().getAddress(), bleReadOrWriteCallback);

            } else {
                currDevice.setConntect(false);
            }
            myhandler.post(showbtn);
        }

        @Override
        public void onFail(int errorCode) {
            Log.e(TAG, "连接失败：" + errorCode);

        }
    };
    private BleScanResultCallback resultCallback = new BleScanResultCallback() {
        @Override
        public void onSuccess() {

            Log.d(TAG, "开启扫描成功");
        }

        @Override
        public void onFail() {
            Log.d(TAG, "开启扫描失败");
        }

        @Override
        public void onFindDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG, "扫描到新设备：" + device.getName() + "     " + device.getAddress());
            if (devicelist != null && device.getName() != null) {
                //判断是否已存在
                boolean canadd = true;
                for (BLEDevice temp : devicelist) {
                    if (temp.getBluetoothDevice().getAddress().equals(device.getAddress())) {
                        //已存在则更新rssi
                        temp.setRssi(rssi);
                        canadd = false;
                        Log.d(TAG, "更新rssi");
                    }
                }
                if (canadd) {
                    //不存在则添加
                    BLEDevice newdevice = new BLEDevice();
                    newdevice.setBluetoothDevice(device);
                    newdevice.setRssi(rssi);
                    devicelist.add(newdevice);
                    Log.d(TAG, "新设备已添加");
                }
                myhandler.post(refreshAdapter);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        initListview();


    }

    private void initListview() {
        adapter = new ScanResultAdapter(this, devicelist);
        main_list_scanresule.setAdapter(adapter);
        main_list_scanresule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //为了保持唯一的连接  先断开之前的 这里根据具体需求决定要不要
                if (currDevice != null && currDevice.isConntect()) {
                    mBle.disconnect(currDevice.getBluetoothDevice().getAddress());
                    Log.d(TAG, "发现之前的gatt已连接 现在开始断开");
                }
                currDevice = devicelist.get(position);
                if (currDevice == null) {
                    Log.e(TAG, "currDevice == null   refresh device list");
                    return;
                }
                boolean connect_resule = mBle.requestConnect(currDevice.getBluetoothDevice().getAddress(), connectionCallback, true);
                if (connect_resule) {

                } else {

                }


            }
        });
    }

    private void initview() {
        main_btn_startorstop = (Button) findViewById(R.id.main_btn_startorstop);
        main_btn_disconnect = (Button) findViewById(R.id.main_btn_disconnect);
        main_btn_services = (Button) findViewById(R.id.main_btn_services);
        main_btn_write = (Button) findViewById(R.id. main_btn_write);
        main_list_scanresule = (ListView) findViewById(R.id.main_list_scanresule);

        main_btn_startorstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mScanning) {
                    myhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            main_btn_startorstop.setText("停止");
                        }
                    });
                    openBluetoothScanDevice();
                } else {
                    myhandler.post(stopRunnable);
                }

            }
        });

        main_btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currDevice != null && currDevice.isConntect()) {
                    mBle.disconnect(currDevice.getBluetoothDevice().getAddress());
                }
            }
        });

        main_btn_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ServicesList.class);
                i.putExtra("type",3);
                startActivity(i);
            }
        });
        main_btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DataActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * 检测蓝牙是否打开
     */
    void openBluetoothScanDevice() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            //蓝牙没打开则去打开蓝牙
            boolean openresult = toEnable(BluetoothAdapter.getDefaultAdapter());
            if (!openresult) {
                Toast.makeText(MainActivity.this, "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
                return;
            }
            //停个半秒再检查一次
            SystemClock.sleep(500);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    while (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (i >= 15) {
                            Toast.makeText(MainActivity.this, "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
                            break;
                        } else {
                            i++;
                        }

                    }
                    //发现蓝牙打开了，则进行开启扫描的步骤
                    scanDevice();
                }
            });
        } else {
            //检查下当前是否在进行扫描 如果是则先停止
            if (mBle != null && mScanning) {
                mBle.stopScan();
            }
            scanDevice();
        }
    }

    private boolean toEnable(BluetoothAdapter bluertoothadapter) {
        //TODO 启动蓝牙
        boolean result = false;
        try {
            for (Method temp : Class.forName(bluertoothadapter.getClass().getName()).getMethods()) {
                if (temp.getName().equals("enableNoAutoConnect")) {
                    result = (boolean) temp.invoke(bluertoothadapter);
                }
            }
        } catch (Exception e) {
            //反射调用失败就启动通过enable()启动;
            result = bluertoothadapter.enable();
            Log.d(TAG, "启动蓝牙的结果:" + result);
            e.printStackTrace();

        }
        return result;

    }

    @UiThread
    void scanDevice() {
        //如果此时发蓝牙工作还是不正常 则打开手机的蓝牙面板让用户开启
        if (mBle != null && !mBle.adapterEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        myhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //检查一下去那些，如果没有则动态请求一下权限
                requestPermission();
                //开启扫描
                scanLeDevice(true);
            }
        }, 500);
    }

    private void scanLeDevice(final boolean enable) {
        //获取ble操作类
        mBle = AppContext.getInstance().getmBle();
        if (mBle == null) {
            return;
        }
        if (enable) {
            //开始扫描
            if (mBle != null) {
                boolean startscan = mBle.startScan(resultCallback);
                if (!startscan) {
                    Toast.makeText(MainActivity.this, "开启蓝牙扫描失败，请检查蓝牙是否正常工作！", Toast.LENGTH_LONG).show();
                    return;
                }
                mScanning = true;
                //扫描一分钟后停止扫描
                myhandler.postDelayed(stopRunnable, SCAN_PERIOD);
            }
        } else {
            //停止扫描
            mScanning = false;
            if (mBle != null) {
                mBle.stopScan();
                myhandler.removeCallbacksAndMessages(null);
            }
        }

    }

    synchronized private void requestPermission() {
        //TODO 向用户请求权限
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH}, REQUEST_FINE_LOCATION);
            Toast.makeText(MainActivity.this, "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
            MainActivity.this.finish();
            return;
        } else {

        }
    }
}
