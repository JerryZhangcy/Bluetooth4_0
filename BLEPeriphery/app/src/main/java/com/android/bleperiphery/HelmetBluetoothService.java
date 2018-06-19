package com.android.bleperiphery;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;

public class HelmetBluetoothService extends Service implements OnReceiverCallback {
    public static boolean mServiceRunning = false;
    private HelmetBluetoothManager mHelmetBluetoothManager;
    public static final String ACTION = "com.helmet.recv.data";
    public static final String DATA_KEY = "recv_data";

    @Override
    public void onCreate() {
        super.onCreate();
        mServiceRunning = true;
        mHelmetBluetoothManager = HelmetBluetoothManager.getInsatnce();
        mHelmetBluetoothManager.startAdvertising();
        mHelmetBluetoothManager.setOnReceiverCallback(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceRunning = false;
        if (mHelmetBluetoothManager != null)
            mHelmetBluetoothManager.stopAdvertising();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCharacteristicWriteRequest(BluetoothGattCharacteristic characteristicRead,
                                             BluetoothGattServer bluetoothGattServer, BluetoothDevice device, byte[] value) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(value);
        String context = new String(byteBuffer.array());
        Log.e(HelmetBluetoothManager.TAG, "[onCharacteristicWriteRequest] ---HelmetBluetoothService---" +
                context);
        Intent i = new Intent(ACTION);
        i.putExtra(DATA_KEY, context);
        sendBroadcast(i);
        String res = "world!";
        characteristicRead.setValue(res.getBytes());
        bluetoothGattServer.notifyCharacteristicChanged(device, characteristicRead, false);
    }

    @Override
    public void onConnectionStateChange(int newState) {

    }
}
