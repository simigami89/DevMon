package com.example.controller;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurvalsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurvalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurvalsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static TextView curVal_I;
    static TextView curVal_I_B;
    static TextView curVal_I_C;
    static TextView curVal_U_A;
    static TextView curVal_U_B;
    static TextView curVal_U_C;
    static TextView rssi_value;

    static ImageView rssi_icon;
    //static ImageButton switch_button;
    private static CurVals mCurVals;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment CurvalsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        //curVal_U_A = (TextView) rootView.findViewById(R.id.cur_val);
//        curVal_I_B = (TextView) rootView.findViewById(R.id.cur_val_I_B);
//        curVal_U_B = (TextView) rootView.findViewById(R.id.cur_val_U_B);
//        curVal_I_C = (TextView) rootView.findViewById(R.id.cur_val_I_C);
//        curVal_U_C = (TextView) rootView.findViewById(R.id.cur_val_U_C);
        //rssi_value = (TextView) rootView.findViewById(R.id.rssi_value);
        rssi_icon = (ImageView) rootView.findViewById(R.id.rssi_indication);
        //switch_button = (ImageButton) rootView.findViewById(R.id.switch_button);
        return rootView;
    }

    public static void showValues(){
        curVal_I.setText(mCurVals.getI_A());
//        curVal_U_A.setText(mCurVals.getU_A());
//        curVal_I_B.setText(mCurVals.getI_B());
//        curVal_U_B.setText(mCurVals.getU_B());
//        curVal_I_C.setText(mCurVals.getI_C());
//        curVal_U_C.setText(mCurVals.getU_C());
        //rssi_value.setText(mCurVals.getRSSI());
//        if(mCurVals.isStateFlag())
//            switch_button.setImageResource(R.drawable.button_on);
//        else
            //switch_button.setImageResource(R.drawable.button_off);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
