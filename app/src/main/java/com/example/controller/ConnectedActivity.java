package com.example.controller;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ConnectedActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static String CHAR_CUR_VAL_I_A = "5ac70ac1-717e-11e5-a837-0800200c9a66";
    public static String CHAR_CUR_VAL_I_B = "5ac70ac3-717e-11e5-a837-0800200c9a66";
    public static String CHAR_CUR_VAL_I_C = "5ac70ac5-717e-11e5-a837-0800200c9a66";
    public static String CHAR_CUR_VAL_U_A = "5ac70ac2-717e-11e5-a837-0800200c9a66";
    public static String CHAR_CUR_VAL_U_B = "5ac70ac4-717e-11e5-a837-0800200c9a66";
    public static String CHAR_CUR_VAL_U_C = "5ac70ac6-717e-11e5-a837-0800200c9a66";

    public static String SERVICE_FLAG_CONTROL = "5ac7f1a0-717e-11e5-a837-0800200c9a66";
    public static String CHAR_STATE_FLAG = "5ac7F1A1-717e-11e5-a837-0800200c9a66";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String SERVICE_CUR_VALS = "5ac70ac0-717e-11e5-a837-0800200c9a66";

    private static BluetoothGattCharacteristic gattCharacteristic1;
    private static BluetoothGattService mBluetoothGattServiceCurVals;
    private static BluetoothGattService mBluetoothGattServiceFlagControl;

    static ArrayList<BluetoothGattService> deviceServices = new ArrayList();

    private MenuItem connectBtn;

    private static BluetoothManager mBluetoothManager;
    private static BluetoothDevice mDevice;
    private static BluetoothGatt mBluetoothGatt;

    final static String DEBUG = ConnectedActivity.class.getName();

    private static Handler mHandler;
    private static Timer rssiTimer;
    private static TimerTask task;

    private static Timer configTimer;
    private static TimerTask taskConfig;

    private static Timer readCharTimer;
    private static TimerTask taskReadCharConfig;

    final int STATE_CONNECTING = 0;
    final int STATE_CONNECTED = 1;
    final int STATE_DISCONNECTED = 2;
    final int UPDATE_CURVALS = 3;
    final int CHANGE_BUTTON_ICON = 4;

    static int charEnabledCount = 0;
    static int charReadCount = 0;

    static boolean stateFlag = false; //true -нажата

    public CurVals mCurVals;
    TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Connecting...");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG, "back");
                disconnect();
                finish();
            }
        });


        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == UPDATE_CURVALS) {
                    CurvalsFragment.showValues();
                    CurvalsFragment.ChangeRSSIicon();
//                    textView.append(" ");
                }
                 else if (msg.what == STATE_DISCONNECTED) {
                    toolbar.setSubtitle("Disconncected");
                    connectBtn.setIcon(R.drawable.ic_check_circle_white_24dp);
                } else if (msg.what == STATE_CONNECTED) {
                    connectBtn.setIcon(R.drawable.ic_cancel_white_24dp);
                    toolbar.setSubtitle("Connected");
                } else if (msg.what == STATE_CONNECTING) {
                    toolbar.setSubtitle("Connecting...");
                }else if(msg.what == CHANGE_BUTTON_ICON){
                    ButtonFragment.changeIcon(stateFlag);
                }

            }
        };

        mCurVals = CurVals.getInstance();
    }

    @Override
    protected void onDestroy() {
        disconnect();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mDevice = (BluetoothDevice) getIntent().getParcelableExtra("device");
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        connect();
        Log.d(DEBUG, "onResume");
        super.onResume();
    }

    private void connect() {
        if (mBluetoothGatt == null) {
            mBluetoothGatt = mDevice.connectGatt(getApplicationContext(), false, mGattCallback);
            Log.d(DEBUG, "connectGatt()");
            mHandler.sendMessage(mHandler.obtainMessage(STATE_CONNECTING));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connected, menu);
        connectBtn = menu.findItem(R.id.btn_toolbar);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Uri address = Uri.parse("http://antraks.ru");
            Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
            startActivity(openlinkIntent);
            return true;
        }
        else if (id == R.id.btn_toolbar) {
            if(mBluetoothManager.getConnectionState(mDevice,7) == BluetoothProfile.STATE_CONNECTED){
                disconnect();
            }
            else if(mBluetoothManager.getConnectionState(mDevice,7) == BluetoothProfile.STATE_DISCONNECTED){
                connect();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void disconnect() {
        if (mBluetoothGatt != null) {
            try {
                configTimer.cancel();
                taskConfig.cancel();
                rssiTimer.cancel();
                task.cancel();
                readCharTimer.cancel();
                taskReadCharConfig.cancel();
            } catch (Exception e) {
                Log.d(DEBUG, "timer = NULL");
            }
            mBluetoothGatt.disconnect();
        }
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == 2) {
                Log.d(ConnectedActivity.DEBUG, "Connected");
                Log.d(ConnectedActivity.DEBUG, "Searching for services");
                mHandler.sendMessage(mHandler.obtainMessage(STATE_CONNECTED));
                ConnectedActivity.mBluetoothGatt.discoverServices();
                rssiTimer = new Timer();
                task = new TimerTask() {
                    public void run() {
                        mBluetoothGatt.readRemoteRssi();
                    }
                };
                rssiTimer.schedule(task, 2000, 2000);
            } else if (newState == 0) {
                Log.d(ConnectedActivity.DEBUG, "Device disconnected");
                mCurVals.setRSSI(0);
                mHandler.sendMessage(mHandler.obtainMessage(UPDATE_CURVALS));
                try {
                    rssiTimer.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.sendMessage(mHandler.obtainMessage(STATE_DISCONNECTED));
                mBluetoothGatt.close();
                mBluetoothGatt = null;
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == 0) {
                int countServices = 0;
                for (BluetoothGattService gattService : mBluetoothGatt.getServices()) {
                    Log.d(DEBUG, "Service discovered: " + gattService.getUuid());
                    if (SERVICE_FLAG_CONTROL.equals(gattService.getUuid().toString())) {
                        mBluetoothGattServiceFlagControl = gattService;
                        deviceServices.add(mBluetoothGattServiceFlagControl);
                        Log.d(DEBUG, "Found FlagControl Service");
                        countServices++;
                    }
                    if (SERVICE_CUR_VALS.equals(gattService.getUuid().toString())) {
                        mBluetoothGattServiceCurVals = gattService;
                        deviceServices.add(mBluetoothGattServiceCurVals);
                        Log.d(DEBUG, "Found cur_vals Service");


                        countServices++;
                    }
                }
                if(countServices == 2)
                    enableNotification();
                return;
            }
            Log.d(ConnectedActivity.DEBUG, "onServicesDiscovered received: " + status);
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (status == 0) {
                String access$000 = ConnectedActivity.DEBUG;
                Object[] objArr = new Object[1];
                objArr[0] = Integer.valueOf(rssi);
                Log.d(access$000, String.format("BluetoothGatt ReadRssi[%d]", objArr));
                mCurVals.setRSSI(rssi);
                mHandler.sendMessage(mHandler.obtainMessage(UPDATE_CURVALS));
            }
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            String uuidString = characteristic.getUuid().toString();
            String str = null;
            try {
                str = new String(characteristic.getValue(), "UTF-8");
                //textView.append(str);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (uuidString.equals(ConnectedActivity.CHAR_CUR_VAL_I_A)) {
                mCurVals.setI_A(Integer.parseInt(str));
            } else if (uuidString.equals(ConnectedActivity.CHAR_CUR_VAL_U_A)) {
                mCurVals.setU_A(Integer.parseInt(str));
            }else if (uuidString.equals(ConnectedActivity.CHAR_STATE_FLAG)) {
                stateFlag = !stateFlag;
                mCurVals.setStateFlag(stateFlag);
            }
           mHandler.sendMessage(mHandler.obtainMessage(ConnectedActivity.this.UPDATE_CURVALS));
            Log.d(DEBUG, str + " " + characteristic.getUuid().toString());
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(DEBUG, characteristic.toString() + " status: " + Integer.toString(status));
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            byte[] tx = characteristic.getValue();
            String str = null;
            try {
                str = new String(tx, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (characteristic.getUuid().equals(UUID.fromString(CHAR_STATE_FLAG))) {
                if(Integer.parseInt(str)!=0) {
                    mCurVals.setStateFlag(true);
                }else mCurVals.setStateFlag(false);
                ButtonFragment.changeIcon(mCurVals.isStateFlag());
            }
            Log.d(ConnectedActivity.DEBUG, str);
        }

    };

    public String byteToHex(byte[] b) {
        String str = "";
        for (int i = 0; i < b.length; i ++) {
            str = str + Integer.toHexString(b[i] & 255);
        }
        return str;
    }

    private void enableNotification() {

        charEnabledCount = 0;
        if (configTimer != null) {
            configTimer.cancel();
        }
        if (taskConfig != null) {
            taskConfig.cancel();
        }
        configTimer = new Timer();
        taskConfig = new TimerTask() {
            public void run() {

                switch (charEnabledCount) {
                    case 0:
                        gattCharacteristic1 = mBluetoothGattServiceCurVals.getCharacteristic(UUID.fromString(CHAR_CUR_VAL_I_A));
                        mBluetoothGatt.setCharacteristicNotification(ConnectedActivity.gattCharacteristic1, true);
                        break;
                    case 1:
                        gattCharacteristic1 = mBluetoothGattServiceCurVals.getCharacteristic(UUID.fromString(CHAR_CUR_VAL_U_A));
                        mBluetoothGatt.setCharacteristicNotification(ConnectedActivity.gattCharacteristic1, true);
                        break;
                    case 2:
                        gattCharacteristic1 = mBluetoothGattServiceFlagControl.getCharacteristic(UUID.fromString(CHAR_STATE_FLAG));
                        mBluetoothGatt.setCharacteristicNotification(ConnectedActivity.gattCharacteristic1, true);
                        break;
                }
                BluetoothGattDescriptor descriptor = gattCharacteristic1.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
                charEnabledCount++;
                if (charEnabledCount == 3) {
                    configTimer.cancel();
                    mBluetoothGatt.readCharacteristic(mBluetoothGattServiceFlagControl.getCharacteristic(UUID.fromString(CHAR_STATE_FLAG)));
                }
            }
        };

        if (charEnabledCount == 1) {
                    configTimer.cancel();
        }
        configTimer.schedule(taskConfig, 2000, 2000);
    }



    public static void writeCharacteristicValue(String service, String characteristic, byte[] bytes, View view) {
        if (mBluetoothGatt != null) {
            for (BluetoothGattService gattService : deviceServices) {
                if (service.equals(gattService.getUuid().toString())) {
                    gattCharacteristic1 = gattService.getCharacteristic(UUID.fromString(characteristic));
                    gattCharacteristic1.setValue(bytes);
                    mBluetoothGatt.writeCharacteristic(gattCharacteristic1);
                    Log.d(DEBUG, "try write char");
                }
            }
            return;
        }
       // Toast.makeText(view.getContext(), "Device not connected", Toast.LENGTH_SHORT).show();
    }

    public void switch_indication(View view) {
        byte [] tx = new byte[1];
        if(stateFlag) {
            tx[0]=0x00;
        }else {
            tx[0]=0x01;
        }
        writeCharacteristicValue(SERVICE_FLAG_CONTROL, CHAR_STATE_FLAG, tx, view);
        stateFlag = !stateFlag;
        Log.d(DEBUG,Boolean.toString(stateFlag));
        mCurVals.setStateFlag(stateFlag);
        mHandler.sendMessage(mHandler.obtainMessage(CHANGE_BUTTON_ICON));

    }
}
