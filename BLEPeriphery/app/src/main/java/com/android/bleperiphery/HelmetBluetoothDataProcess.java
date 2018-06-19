package com.android.bleperiphery;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

public class HelmetBluetoothDataProcess {
    private static final String TAG = "HelmetBluetoothData";
    private static final int UNKNOWN_COMMAND = -1;
    private static final int WIFI_SETTING = 1;
    private static final int SERVER_SETTING = 2;
    private static final int STORAGE_SETTING = 3;
    private static final int VOLUME_SETTING = 4;
    private static final int QUERY_SETTING = 5;
    private static final int NETWORK_SETTING = 6;

    public static void onReceiveMessage(String message,
                                        BluetoothGattCharacteristic characteristicRead,
                                        BluetoothGattServer bluetoothGattServer,
                                        BluetoothDevice device) {
        if (message != null && !TextUtils.isEmpty(message.trim())) {
            try {
                JSONObject json = new JSONObject(message);
                int msgId = json.optInt("msg_id", UNKNOWN_COMMAND);
                switch (msgId) {
                    case WIFI_SETTING:
                        setWifi(json, characteristicRead, bluetoothGattServer, device);
                        break;
                    case SERVER_SETTING:
                        setServer(json, characteristicRead, bluetoothGattServer, device);
                        break;
                    case STORAGE_SETTING:
                        setStorage(json, characteristicRead, bluetoothGattServer, device);
                        break;
                    case VOLUME_SETTING:
                        setVolume(json, characteristicRead, bluetoothGattServer, device);
                        break;
                    case QUERY_SETTING:
                        querySetting(characteristicRead, bluetoothGattServer, device);
                        break;
                    case NETWORK_SETTING:
                        setNetwork(json, characteristicRead, bluetoothGattServer, device);
                        break;

                    default:
                        Log.e(TAG, "unrecognized command: " + message);
                }
            } catch (Exception var4) {
            }

        } else {
            Log.e(TAG, "receive empty message from bluetooth...");
        }
    }


    public static void setWifi(JSONObject json,
                               BluetoothGattCharacteristic characteristicRead,
                               BluetoothGattServer bluetoothGattServer,
                               BluetoothDevice device) {

    }

    public static void setServer(JSONObject json,
                                 BluetoothGattCharacteristic characteristicRead,
                                 BluetoothGattServer bluetoothGattServer,
                                 BluetoothDevice device) {

    }

    public static void setStorage(JSONObject json,
                                  BluetoothGattCharacteristic characteristicRead,
                                  BluetoothGattServer bluetoothGattServer,
                                  BluetoothDevice device) {

    }

    public static void setVolume(JSONObject json,
                                 BluetoothGattCharacteristic characteristicRead,
                                 BluetoothGattServer bluetoothGattServer,
                                 BluetoothDevice device) {

    }

    public static void querySetting(BluetoothGattCharacteristic characteristicRead,
                                    BluetoothGattServer bluetoothGattServer,
                                    BluetoothDevice device) {

    }

    public static void setNetwork(JSONObject json, BluetoothGattCharacteristic characteristicRead,
                                  BluetoothGattServer bluetoothGattServer,
                                  BluetoothDevice device) {

    }

    public static void setHelmetName(String name) {

    }
}
