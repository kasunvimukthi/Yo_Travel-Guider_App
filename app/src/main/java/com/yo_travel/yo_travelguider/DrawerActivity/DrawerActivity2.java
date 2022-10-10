package com.yo_travel.yo_travelguider.DrawerActivity;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yo_travel.yo_travelguider.DrawerActivity2.DeleteMe;
import com.yo_travel.yo_travelguider.DrawerActivity2.My_Info;
import com.yo_travel.yo_travelguider.DrawerActivity2.Password;
import com.yo_travel.yo_travelguider.DrawerActivity2.QRcode;
import com.yo_travel.yo_travelguider.R;

public class DrawerActivity2 extends Fragment {

    SharedPreferences sharedPreferences;

    private static final String SHARED_NAME = "my";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASS = "Pass";
    private static final String KEY_U_ID = "";

    Button button1, button2, button3, button4;

    Activity context1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context1 = getActivity();
        View view = inflater.inflate(R.layout.fragment_drawer_activity2, container, false);

        sharedPreferences = context1.getSharedPreferences(SHARED_NAME,MODE_PRIVATE);
        String U_ID = sharedPreferences.getString(KEY_U_ID,null);
        String U_Email = sharedPreferences.getString(KEY_EMAIL,null);
        String U_Pass = sharedPreferences.getString(KEY_PASS,null);

        button1 = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button3);
        button3 = view.findViewById(R.id.button4);
        button4 = view.findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context1, My_Info.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("U_ID",U_ID);
                intent.putExtra("U_Email",U_Email);
                context1.startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context1, Password.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("U_ID",U_ID);
                intent.putExtra("U_Email",U_Email);
                context1.startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context1, QRcode.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("U_Email",U_Email);
                intent.putExtra("U_Pass",U_Pass);
                context1.startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context1, DeleteMe.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("U_ID",U_ID);
                intent.putExtra("U_Pass",U_Pass);
                intent.putExtra("U_Email",U_Email);
                context1.startActivity(intent);
            }
        });
        return view;
    }
}