package com.example.controller;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Екатерина Захарова on 25.02.2016.
 */
 class BleDeviceInfo {
    private BluetoothDevice mBtDevice;
    private int mRssi;

    BleDeviceInfo(BluetoothDevice device, int rssi) {
        this.mBtDevice = device;
        this.mRssi = rssi;
    }

     public BluetoothDevice getBluetoothDevice() {
        return this.mBtDevice;
    }

     int getRssi() {
        return this.mRssi;
    }

     void updateRssi(int rssiValue) {
        this.mRssi = rssiValue;
    }
}
