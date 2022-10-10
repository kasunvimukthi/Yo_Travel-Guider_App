package com.yo_travel.yo_travelguider.DrawerActivity1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.yo_travel.yo_travelguider.Adapter.Adapter1;
import com.yo_travel.yo_travelguider.DB_Connection.Conn;
import com.yo_travel.yo_travelguider.DrawerActivity.DrawerActivity1;
import com.yo_travel.yo_travelguider.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Activity1 extends AppCompatActivity {

    WebView webView;

    String MAP,P_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        Intent i = getIntent();
        String map = i.getStringExtra("MAP");
        String p_id = i.getStringExtra("P_ID");

        P_ID = p_id;
        MAP = map;

//        Toast.makeText(this, ""+P_ID, Toast.LENGTH_SHORT).show();
        webView = findViewById(R.id.WebView1);

        webView.loadUrl(MAP);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);

    }


}