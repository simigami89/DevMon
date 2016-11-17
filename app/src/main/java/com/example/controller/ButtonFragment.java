package com.example.controller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonFragment extends Fragment {

    static ImageView button;
    public ButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_button, container, false);
                button = (ImageView) rootView.findViewById(R.id.switch_button);
        // Inflate the layout for this fragment
        return rootView;
    }

    public static void changeIcon(boolean flag){
        if(flag) button.setImageResource(R.drawable.button_on);
        else button.setImageResource(R.drawable.button_off);
    }

}
