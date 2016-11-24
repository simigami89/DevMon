package com.example.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CurvalsFragment extends Fragment {


    static TextView curVal_I;
    static TextView indication_text;

    static ImageView rssi_icon;
    static ImageView voltag_icon;
    private static CurVals mCurVals;

    private OnFragmentInteractionListener mListener;

    public static CurvalsFragment newInstance() {
        CurvalsFragment fragment = new CurvalsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CurvalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_curvals, container, false);
        mCurVals = CurVals.getInstance();

        curVal_I = (TextView) rootView.findViewById(R.id.cur_val_I);
        indication_text = (TextView) rootView.findViewById(R.id.indication_text);
        rssi_icon = (ImageView) rootView.findViewById(R.id.rssi_indication);
        voltag_icon = (ImageView) rootView.findViewById(R.id.voltage_icon);
        return rootView;
    }

    public static void showValues(){
        curVal_I.setText(String.valueOf(mCurVals.getI())+" units");

        if(mCurVals.getU()>10)
            voltag_icon.setImageResource(R.drawable.ic_voltag_on);
        else voltag_icon.setImageResource(R.drawable.ic_voltag_off);

        if(mCurVals.isStateFlag()){
            indication_text.setText("Indication on");
        }
        else indication_text.setText("Indication off");
        ButtonFragment.changeIcon(mCurVals.isStateFlag());
    }

    public static void ChangeRSSIicon(){
        if(mCurVals.getRSSI()==0)
            rssi_icon.setImageResource(R.drawable.rssi_null);
        else if(mCurVals.getRSSI() < -80)
            rssi_icon.setImageResource(R.drawable.rssi_80);
        else if(mCurVals.getRSSI() < -70)
            rssi_icon.setImageResource(R.drawable.rssi_70);
        else if(mCurVals.getRSSI() < -60)
            rssi_icon.setImageResource(R.drawable.rssi_60);
        else if(mCurVals.getRSSI() < -50)
            rssi_icon.setImageResource(R.drawable.rssi_50);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {


    }

}
