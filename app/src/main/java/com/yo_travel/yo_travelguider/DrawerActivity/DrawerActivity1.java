package com.yo_travel.yo_travelguider.DrawerActivity;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.yo_travel.yo_travelguider.Adapter.Adapter1;
import com.yo_travel.yo_travelguider.AdapterData.AdapterData1;
import com.yo_travel.yo_travelguider.ConstantsV;
import com.yo_travel.yo_travelguider.DB_Connection.Conn;
import com.yo_travel.yo_travelguider.Location.MyLocation;
import com.yo_travel.yo_travelguider.R;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


@SuppressWarnings("ALL")
public class DrawerActivity1 extends Fragment {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    Adapter1 adapter1;

    ArrayList<AdapterData1> list1;

    RecyclerView recyclerView1;

    ConstraintLayout constraintLayout1,constraintLayout2;

    TextView days1,hours1,min1,sec1,tvEventStart,textView17,textView18,textView19,textView20,textView31;

    Switch switch1;

    private Handler handler;
    private Runnable runnable;

    Activity context1;

    String U_ID;
    String countDownStart1 = null;
    String countDownStart2 = null;

    Double Lat,Lon;

    SharedPreferences sharedPreferences;

    private static final String SHARED_NAME = "my";
    private static final Boolean SWITCH1 = true;

    public DrawerActivity1(String u_id) {
        this.U_ID = u_id;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context1 = getActivity();
        View view = inflater.inflate(R.layout.fragment_drawer_activity1, container, false);

        days1 = view.findViewById(R.id.textView8);
        hours1 = view.findViewById(R.id.textView9);
        min1 = view.findViewById(R.id.textView10);
        sec1 = view.findViewById(R.id.textView11);
        tvEventStart = view.findViewById(R.id.textView5);
        textView17 = view.findViewById(R.id.textView17);
        textView18 = view.findViewById(R.id.textView18);
        textView19 = view.findViewById(R.id.textView19);
        textView20 = view.findViewById(R.id.textView20);
        textView31 = view.findViewById(R.id.textView31);

        textView31.setText(getCurrentTime());

        switch1 = view.findViewById(R.id.switch1);

        constraintLayout1 = view.findViewById(R.id.Constrain1);
        constraintLayout2 = view.findViewById(R.id.Constrain2);

        recyclerView1 = view.findViewById(R.id.recyclerView);

        tvEventStart.setVisibility(View.GONE);
        constraintLayout1.setVisibility(View.GONE);

        list1 = new ArrayList<>();

        Getdata1 getdata1 = new Getdata1();
        getdata1.execute();

        Getdata2 getdata2 = new Getdata2();
        getdata2.execute();

        sharedPreferences = context1.getSharedPreferences(SHARED_NAME,MODE_PRIVATE);

        Boolean sw = sharedPreferences.getBoolean(String.valueOf(SWITCH1),false);

        if (sharedPreferences.getBoolean(String.valueOf(SWITCH1),true)){
            if (ContextCompat.checkSelfPermission(
                    context1.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                        context1,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_LOCATION_PERMISSION
                );
            }else{
                startLocationService();
            }
        }else {
            stopLocationService();
        }
        switch1.setChecked(sw);

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch1.isChecked()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(String.valueOf(SWITCH1),true);
                    editor.apply();

                    if (ContextCompat.checkSelfPermission(
                            context1.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(
                                context1,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_LOCATION_PERMISSION
                        );
                    }else{
                        startLocationService();
                    }

                    switch1.setChecked(true);
                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(String.valueOf(SWITCH1),false);
                    editor.apply();

                    stopLocationService();

                    switch1.setChecked(false);
                }
            }
        });

        return view;

    }

    private String getCurrentTime(){
        String time = new SimpleDateFormat("hh:mm a",Locale.getDefault()).format(new Date());
        String Date = new SimpleDateFormat("yyyy-MMMM-dd",Locale.getDefault()).format(new Date());
        String DateTime = ""+time+", On "+Date;
        return DateTime;
    }

    private class Getdata1 extends AsyncTask<String, Void, String> {

        String res = "";
        String name = "";
        String s_date = "";
        String e_date = "";
        String id = "";
        String pid = "";
        String status = "1";
        String status1 = "Full Paid";
        String adults ="";
        String child ="";
        String total ="";
        String map ="";

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Conn.url, Conn.user, Conn.pass);

                String result = "";

                Statement st1 = con.createStatement();
                ResultSet rs1 = st1.executeQuery("SELECT * FROM `guider_alocate` WHERE `G_ID` = '"+U_ID+"'");

                while (rs1.next()) {

                    pid = rs1.getString(3).toString();

                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM `package` WHERE Travel_ID = "+ pid +" ORDER BY `T_Start_Date`");

                    while (rs.next()) {
                        name =  rs.getString(3).toString();
                        id =  rs.getString(1).toString();
                        s_date = rs.getString(12);
                        e_date = rs.getString(13);
                        map = rs.getString("T_Map");

                        String query2 = "SELECT SUM(`U_adults`) AS `U_adults` FROM `invoice` WHERE T_ID = "+ id +" AND `T_start_date` = '"+ s_date +"' AND `T_end_date` = '"+ e_date +"' AND `Status` = '"+status1+"'";
                        Statement st2 = con.createStatement();
                        ResultSet rs2 = st2.executeQuery(query2);

                        while (rs2.next()) {

                                String adults1 = rs2.getString("U_adults");
//                                result = adults;
                                if (adults1 == null){
                                    adults = "0";
                                }else{
                                    adults = adults1;
                                }
                        }

                        String query3 = "SELECT SUM(`U_children`) AS `U_children` FROM `invoice` WHERE T_ID = "+ id +" AND `T_start_date` = '"+ s_date +"' AND `T_end_date` = '"+ e_date +"' AND `Status` = '"+status1+"'";
                        Statement st3 = con.createStatement();
                        ResultSet rs3 = st3.executeQuery(query3);

                        while (rs3.next()) {

                               String child1 = rs3.getString("U_children");
                            if (child1 == null){
                                child = "0";
                            }else{
                                child = child1;
                            }

                        }
                        int A = Integer.parseInt(adults);
                        int B = Integer.parseInt(child);
                        int C = A+B;
                        total = String.valueOf(C);

                        list1.add(new AdapterData1(name,id,s_date,e_date,adults,child,total,map));
                    }
                }
                res = result;
            }catch (Exception e){
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(context1, ""+result, Toast.LENGTH_LONG).show();
            LinearLayoutManager layoutManager = new  LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            adapter1 = new Adapter1(getContext(),list1);
            recyclerView1.setAdapter(adapter1);

        }

    }
    private class Getdata2 extends AsyncTask<String, Void, String> {

        String res = "";
        String name = "";
        String s_date = "";
        String e_date = "";
        String id = "";
        String pid = "";
        String status = "1";
        Blob img;
        byte b[];

        String s_date1 = "";
        String e_date1 = "";

        String I_Date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Conn.url, Conn.user, Conn.pass);

                String result = "";

                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM `guider_alocate` INNER JOIN `package` ON guider_alocate.P_ID = package.Travel_ID && `T_Start_Date`> '"+I_Date+"' WHERE `G_ID` = '"+U_ID+"' ORDER BY `T_Start_Date` LIMIT 1");

                    if (rs.next()) {
                        s_date1 = rs.getString("T_Start_Date");

                        result = s_date1;

                    }else{
                        result = "1";
                    }

                res = result;
            }catch (Exception e){
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            countDownStart(result);
//            }

        }

    }

    private void countDownStart(String result) {
        String eDate = result;
        if (eDate.equals('1')){
            Toast.makeText(context1, "You Don't Have Any Guider Allocation1", Toast.LENGTH_SHORT).show();
        }else if(eDate.equals("2")){
            Toast.makeText(context1, "You Don't Have Any Guider Allocation", Toast.LENGTH_SHORT).show();
        }else{
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 1000);
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        // Please here set your event date//YYYY-MM-DD
                        Date futureDate = dateFormat.parse(eDate);
                        Date currentDate = new Date();
                        if (!currentDate.after(futureDate)) {
                            long diff = futureDate.getTime()
                                    - currentDate.getTime();
                            long days = diff / (24 * 60 * 60 * 1000);
                            diff -= days * (24 * 60 * 60 * 1000);
                            long hours = diff / (60 * 60 * 1000);
                            diff -= hours * (60 * 60 * 1000);
                            long minutes = diff / (60 * 1000);
                            diff -= minutes * (60 * 1000);
                            long seconds = diff / 1000;
                            days1.setText("" + String.format("%02d", days));
                            hours1.setText("" + String.format("%02d", hours));
                            min1.setText(""
                                    + String.format("%02d", minutes));
                            sec1.setText(""
                                    + String.format("%02d", seconds));
                            textViewGone1();
                        } else {
                            tvEventStart.setVisibility(View.VISIBLE);
                            tvEventStart.setText("The event started!");
                            textViewGone();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            handler.postDelayed(runnable, 1 * 1000);
        }

    }

    private void textViewGone1() {
        constraintLayout1.setVisibility(View.VISIBLE);
        constraintLayout2.setVisibility(View.GONE);
    }

    private void textViewGone() {
        tvEventStart.setVisibility(View.VISIBLE);
        days1.setVisibility(View.GONE);
        hours1.setVisibility(View.GONE);
        min1.setVisibility(View.GONE);
        sec1.setVisibility(View.GONE);
        textView17.setVisibility(View.GONE);
        textView18.setVisibility(View.GONE);
        textView19.setVisibility(View.GONE);
        textView20.setVisibility(View.GONE);

        constraintLayout1.setVisibility(View.VISIBLE);
        constraintLayout2.setVisibility(View.GONE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }else{
                Toast.makeText(context1, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning(){
        ActivityManager activityManager = (ActivityManager) context1.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null){
            for (ActivityManager.RunningServiceInfo service:
                    activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(MyLocation.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService(){
        if (!isLocationServiceRunning()){
            buildAlertMessageNoGps();
            Intent intent = new Intent(context1.getApplicationContext(),MyLocation.class);
            intent.setAction(ConstantsV.ACTION_START_LOCATION_SERVICE);
            context1.startService(intent);
            Toast.makeText(context1, "Current Location Share Is Started", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context1);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void stopLocationService(){
        if (isLocationServiceRunning()){
            Intent intent = new Intent(context1.getApplicationContext(),MyLocation.class);
            intent.setAction(ConstantsV.ACTION_STOP_LOCATION_SERVICE);
            context1.startService(intent);
            Toast.makeText(context1, "Current Location Share Is Stopped", Toast.LENGTH_SHORT).show();
        }
    }

}