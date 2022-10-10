package com.yo_travel.yo_travelguider.Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.yo_travel.yo_travelguider.Adapter.Adapter1;
import com.yo_travel.yo_travelguider.AdapterData.AdapterData1;
import com.yo_travel.yo_travelguider.ConstantsV;
import com.yo_travel.yo_travelguider.DB_Connection.Conn;
import com.yo_travel.yo_travelguider.DrawerActivity.DrawerActivity1;
import com.yo_travel.yo_travelguider.MainActivity;
import com.yo_travel.yo_travelguider.R;

import java.security.Provider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyLocation extends Service {
    SharedPreferences sharedPreferences;

    private static final String SHARED_NAME = "my";
    private static final String KEY_U_ID = "";

    Double lat,lon;

    TextView textView;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null){
                 lat = locationResult.getLastLocation().getLatitude();
                 lon = locationResult.getLastLocation().getLongitude();
//                Log.e("Location Update",lat+","+lon);

                Getdata1 getdata1 = new Getdata1(lat,lon);
                getdata1.execute();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Yet Implement");
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startLocationService(){
        String chanelID = "Location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                chanelID
        );

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if (notificationManager != null && notificationManager.getNotificationChannel(chanelID) == null){
                NotificationChannel notificationChannel = new NotificationChannel(
                        chanelID,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This Channel Used By Location Service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());


        startForeground(ConstantsV.LOCATION_SERVICE_ID, builder.build());


    }

    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            String action = intent.getAction();
            if (action != null){
                if (action.equals(ConstantsV.ACTION_START_LOCATION_SERVICE)){
                    startLocationService();
                }else if (action.equals(ConstantsV.ACTION_STOP_LOCATION_SERVICE)){
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private class Getdata1 extends AsyncTask<String, Void, String> {

        String res = "";
        String Lat = "";
        String Lon = "";

        public Getdata1(Double lat, Double lon) {
            this.Lat = String.valueOf(lat);
            this.Lon = String.valueOf(lon);
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Conn.url, Conn.user, Conn.pass);

                sharedPreferences = getSharedPreferences(SHARED_NAME,MODE_PRIVATE);

                String id = sharedPreferences.getString(KEY_U_ID,null);

                String query = "UPDATE guider SET Latitude = '"+ Lat +"' , Longitude = '"+Lon+"' WHERE ID = '" + id + "'";
                Statement st1 = con.createStatement();
                st1.executeUpdate(query);

            }catch (Exception e){
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

    }

}
