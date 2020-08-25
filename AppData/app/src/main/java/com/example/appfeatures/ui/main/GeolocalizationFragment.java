package com.example.appfeatures.ui.main;


import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appfeatures.R;
import com.example.appfeatures.views.CustomView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeolocalizationFragment extends Fragment {

    View inf;
    CustomView customView;

    ImageView compass;

    public void showDirection(float azimuth){
        compass.setRotation(azimuth);
    }

    public void drawPath(){
        customView.drawPath();
    }

    public GeolocalizationFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inf =  inflater.inflate(R.layout.fragment_geolocalization, container, false);
        compass = inf.findViewById(R.id.compass);
        customView = inf.findViewById(R.id.customView);

        return inf;
    }

}
