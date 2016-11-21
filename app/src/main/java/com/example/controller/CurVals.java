package com.example.controller;

/**
 * Created by Екатерина Захарова on 26.02.2016.
 */
public class CurVals {
    private static CurVals _instance = null;
    private static int I = 0;
    private static int RSSI = 0;
    private static int U = 0;
    private boolean StateFlag = false;
    private CurVals() {
    }

    boolean isStateFlag() {
        return StateFlag;
    }

    void setStateFlag(boolean stateFlag) {
        StateFlag = stateFlag;
    }

    static synchronized CurVals getInstance() {
        CurVals curVals;
        synchronized (CurVals.class) {
            if (_instance == null) {
                _instance = new CurVals();
            }
            curVals = _instance;
        }
        return curVals;
    }

    void setI_A(int i) {
        I = i;
    }

    void setU_A(int u) {
        U = u;
    }


    void setRSSI(int rssi) {
        RSSI = rssi;
    }

    int getU() {
        return U;
    }

    int getI() {
        return I;
    }

    int getRSSI() {
        return RSSI;
    }
}
