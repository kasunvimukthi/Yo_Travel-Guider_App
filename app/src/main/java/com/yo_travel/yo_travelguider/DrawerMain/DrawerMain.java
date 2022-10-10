package com.yo_travel.yo_travelguider.DrawerMain;

import static com.yo_travel.yo_travelguider.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.navigation.NavigationView;
import com.yo_travel.yo_travelguider.MainActivity;
import com.yo_travel.yo_travelguider.DrawerActivity.DrawerActivity1;
import com.yo_travel.yo_travelguider.DrawerActivity.DrawerActivity2;
import com.yo_travel.yo_travelguider.DrawerActivity.DrawerActivity3;
import com.yo_travel.yo_travelguider.DrawerActivity.DrawerActivity4;


public class DrawerMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences sharedPreferences;

    private static final String SHARED_NAME = "my";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_U_ID = "";

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String U_ID;

    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_drawer_main);

        toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(id.drawer);

        navigationView = findViewById(id.navigationView);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(id.nav_home);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, string.menu_drawer_open, string.menu_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(color.white));
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        sharedPreferences = getSharedPreferences(SHARED_NAME,MODE_PRIVATE);
        String User = sharedPreferences.getString(KEY_NAME,null);
        String Email = sharedPreferences.getString(KEY_EMAIL,null);
        U_ID = sharedPreferences.getString(KEY_U_ID,null);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id.container, new DrawerActivity1(U_ID));
        fragmentTransaction.commit();

        NavigationView navigationView1  = findViewById(id.navigationView);
        View headerView = navigationView1.getHeaderView(0);

        TextView navUserName = headerView.findViewById(id.navUserName);
        TextView navUserEmail = headerView.findViewById(id.navUserEmail);

        navUserName.setText(User);
        navUserEmail.setText(Email);

        TextView toolBarText = findViewById(id.toolbarName);
        toolBarText.setText("Home");

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        if (menuItem.getItemId() == id.nav_home){

            View item1 = findViewById(id.nav_home);
            item1.setBackgroundColor(getResources().getColor(color.background));

            View item2 = findViewById(id.nav_profile);
            item2.setBackgroundColor(getResources().getColor(color.white));

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(id.container, new DrawerActivity1(U_ID));
            fragmentTransaction.commit();
            TextView toolBarText = findViewById(id.toolbarName);
            toolBarText.setText("Home");
        }
        if (menuItem.getItemId() == id.nav_profile){

            View item1 = findViewById(id.nav_profile);
            item1.setBackgroundColor(getResources().getColor(color.background));

            View item2 = findViewById(id.nav_home);
            item2.setBackgroundColor(getResources().getColor(color.white));

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(id.container, new DrawerActivity2());
            fragmentTransaction.commit();
            TextView toolBarText = findViewById(id.toolbarName);
            toolBarText.setText("My Profile");
        }

        if (menuItem.getItemId() == id.nav_logout){

            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            String name = null;
                            editor.putString(KEY_NAME,name);
                            editor.putString(KEY_U_ID,name);
                            editor.putString(KEY_EMAIL,name);
                            editor.commit();
                            Intent intent = new Intent(DrawerMain.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();

        }
        return false;
    }
}