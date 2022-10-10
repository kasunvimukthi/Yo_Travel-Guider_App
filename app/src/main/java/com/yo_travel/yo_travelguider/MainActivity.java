package com.yo_travel.yo_travelguider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.yo_travel.yo_travelguider.DrawerMain.DrawerMain;
import com.yo_travel.yo_travelguider.Location.MyLocation;
import com.yo_travel.yo_travelguider.Slider.Slider;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private static final String SHARED_NAME = "my";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASS = "Pass";
    private static final String KEY_U_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (!foregroundServiceRunning()) {
//            Intent intent = new Intent(this, MyLocation.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(intent);
//            }
//        }


//        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.StatusBarColor));
        sharedPreferences = getSharedPreferences(SHARED_NAME, MODE_PRIVATE);

        String name = sharedPreferences.getString(KEY_NAME, null);

        if (name != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, DrawerMain.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, Slider.class));
                    finish();
                }
            }, 3000);
        }
    }

//    public boolean foregroundServiceRunning(){
//        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)){
//            if(MyLocation.class.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }



}