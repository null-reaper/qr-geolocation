package com.example.appfeatures.ui.main;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appfeatures.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRHuntFragment extends Fragment {

    View inf;

    TextView pointsText;
    String pointsString;
    TextView pointsLeftText;
    String pointsLeftString;
    TextView couponText;
    String couponString;

    ProgressBar points;

    int numCoupons;
    int point;

    public QRHuntFragment() {
        // Required empty public constructor
    }

    public void go(){
        pointsText.setText("50 Points");
    }

    public void updatePoints(int addPoint){
        point += addPoint;

        if (point >= 100 ){
            numCoupons += 1;
            point -= 100;
            Toast.makeText(getContext(),"Congratulations! You just earned a Free Coupon!", Toast.LENGTH_SHORT).show();
            couponString = "Number of Coupons Earned: " + numCoupons;
            couponText.setText(couponString);
        }

        if (point == 0){
            points.setProgress(1);
        } else{
            points.setProgress(point);
        }
        pointsString = point + " Points";
        pointsText.setText(pointsString);
        pointsLeftString = (100-point) + " Points Till Next Coupon";
        pointsLeftText.setText(pointsLeftString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inf = inflater.inflate(R.layout.fragment_qrhunt, container, false);

        pointsText = inf.findViewById(R.id.pointsText);
        pointsString = "0 Points";
        pointsText.setText(pointsString);

        pointsLeftText = inf.findViewById(R.id.pointsLeftText);
        pointsLeftString = "100 Points Till Next Coupon";
        pointsLeftText.setText(pointsLeftString);

        couponText = inf.findViewById(R.id.couponText);
        couponString = "Number of Coupons Earned: 0";
        couponText.setText(couponString);

        points = inf.findViewById(R.id.points);
        points.setProgress(1);

        numCoupons = 0;
        point = 0;

        return inf;
    }

}
