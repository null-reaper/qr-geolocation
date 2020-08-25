package com.example.appfeatures;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.appfeatures.ui.main.GeolocalizationFragment;
import com.example.appfeatures.ui.main.QRHuntFragment;
import com.example.appfeatures.views.CustomView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appfeatures.ui.main.SectionsPagerAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static String result;

    List<Fragment> fragments;
    QRHuntFragment qrFrag;
    GeolocalizationFragment geoFrag;
    CustomView customView;

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currentAzimuth = 0f;
    private SensorManager mSensorManager;

    public static ArrayList<Double []> points;

    public void rotateCompass(float azimuth){
        fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() > 1){
            geoFrag = (GeolocalizationFragment) fragments.get(1);

            if (geoFrag != null){
                geoFrag.showDirection(azimuth);
            }
        }
    }

    public void scanCode(){
        result = "";
        startActivityForResult(new Intent(this,ScannerActivity.class),5);
    }

    public void add25(View view){
        fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() > 0){
            qrFrag = (QRHuntFragment)fragments.get(0);
            qrFrag.updatePoints(25);
        }
    }

    public void displayText(){
        startActivity(new Intent(this,DisplayActivity.class));
    }

    public void displayMap(){
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=Osaka+Science+Museum,+4-chōme-2-1+Nakanoshima,+Kita-ku,+Ōsaka-shi,+Ōsaka-fu+530-0005&destination=APA+Hotel+Osaka+Higobashi+Ekimae&travelmode=walking");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void checkQR(){
        if (result == ""){
            return;
        } else{
            if (result.substring(0,6).equals("qrhunt")){
                if(result.charAt(6) == 'A'){
                    fragments = getSupportFragmentManager().getFragments();
                    qrFrag = (QRHuntFragment)fragments.get(0);
                    qrFrag.updatePoints(25);
                    Toast.makeText(getApplicationContext(),"Congratulations! You just earned 25 points!", Toast.LENGTH_SHORT).show();
                } else if(result.charAt(6) == 'B'){
                    fragments = getSupportFragmentManager().getFragments();
                    qrFrag = (QRHuntFragment)fragments.get(0);
                    qrFrag.updatePoints(50);
                    Toast.makeText(getApplicationContext(),"Congratulations! You just earned 50 points!", Toast.LENGTH_SHORT).show();
                } else{
                    displayText();
                }
            } else if (result.substring(0,6).equals("geoloc")){
                if (points != null){
                    Toast.makeText(getApplicationContext(),"Geolocation Successful!", Toast.LENGTH_SHORT).show();
                    displayMap();
                } else{
                    displayText();
                }
            } else{
                displayText();
            }
        }
    }

    public void drawPath(){
        fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() > 1){
            geoFrag = (GeolocalizationFragment) fragments.get(1);

            if (geoFrag != null){
                geoFrag.drawPath();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 5)
            checkQR();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        result = "";

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        points = new ArrayList<>();

        customView = findViewById(R.id.customView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float alpha = 0.97f;
        synchronized (this){
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                mGravity[0] = alpha*mGravity[0]+(1-alpha)*sensorEvent.values[0];
                mGravity[1] = alpha*mGravity[1]+(1-alpha)*sensorEvent.values[1];
                mGravity[2] = alpha*mGravity[2]+(1-alpha)*sensorEvent.values[2];
            }
            if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                mGeomagnetic[0] = alpha*mGeomagnetic[0]+(1-alpha)*sensorEvent.values[0];
                mGeomagnetic[1] = alpha*mGeomagnetic[1]+(1-alpha)*sensorEvent.values[1];
                mGeomagnetic[2] = alpha*mGeomagnetic[2]+(1-alpha)*sensorEvent.values[2];
            }

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnetic);
            if (success){
                float orientation[] = new float[3];
                SensorManager.getOrientation(R,orientation);
                azimuth = (float)Math.toDegrees(orientation[0]);
                azimuth = (azimuth+360)%360;

                rotateCompass(-azimuth);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}