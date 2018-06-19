package com.android.bleperiphery;

import android.os.ParcelUuid;

/**
 * Created by lihong on 2017/10/24.
 */

public class Constants {
    public static final ParcelUuid Service_UUID=ParcelUuid.fromString("0000b81d-0000-1000-8000-00805f9b34fb");
    public static final int REQUEST_ENABLE_BT=1;


    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
}
