package com.yo_travel.yo_travelguider.Login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.yo_travel.yo_travelguider.DB_Connection.Conn;
import com.yo_travel.yo_travelguider.DrawerMain.DrawerMain;
import com.yo_travel.yo_travelguider.R;
import com.yo_travel.yo_travelguider.md5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class Login extends AppCompatActivity {

    Button login, QR_Scan;
    TextView userName, userPassword, QR_Results;
    SharedPreferences sharedPreferences;

    Connection con;

    private static final String SHARED_NAME = "my";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASS = "Pass";
    private static final String KEY_U_ID = "";
    private static final Boolean SWITCH1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login_btn);
        QR_Scan = (Button) findViewById(R.id.QR_Scan);

        userName = (EditText) findViewById(R.id.userName);
        userPassword = (EditText) findViewById(R.id.userPassword);
        QR_Results =  findViewById(R.id.textView28);
//        Name = (TextView) findViewById(R.id.user_name1);

        sharedPreferences = getSharedPreferences(SHARED_NAME,MODE_PRIVATE);

        String name = sharedPreferences.getString(KEY_NAME,null);

        if (name != null){
            SCheckLogin scheckLogin = new SCheckLogin();
            scheckLogin.execute("");

        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user1 = (String) userName.getText().toString();
                String pass1 = (String) userPassword.getText().toString();


                if (user1.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(user1).matches()){
                    Toast.makeText(Login.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                } else if (pass1.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                } else {
                    CheckLogin checkLogin = new CheckLogin();
                    checkLogin.execute("");
                }

            }

        });

        QR_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanOptions options = new ScanOptions();
                options.setBeepEnabled(true);
                options.setOrientationLocked(true);
                options.setCaptureActivity(CaptureAct.class);
                barLuncher.launch(options);
            }
        });
    }

    ActivityResultLauncher<ScanOptions> barLuncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null){
            QR_Results.setText(result.getContents());
            CheckLogin2 checkLogin2 = new CheckLogin2();
            checkLogin2.execute("");
        }
    });

    private class CheckLogin extends AsyncTask<String, Void, String> {
        String res = "";


        String user1 = (String) userName.getText().toString();
        //      get user password and it convert to md5
        md5 encryption = new md5(userPassword.getText().toString());
        String pass = encryption.getEncryption();
        String pass1 = pass;

        @Override
        protected String doInBackground(String... params) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Conn.url, Conn.user, Conn.pass);

                String result = "";
                String name = "";
                String email = "";
                String pass = "";
                String u_id = "";


                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM guider WHERE G_Email = '" + user1 + "' AND G_Password ='" + pass1 + "'");
                ResultSetMetaData rsmd = rs.getMetaData();

                if (rs.next()) {

                    u_id = rs.getString(1).toString();
                    name = rs.getString(2).toString();
                    email = rs.getString(4).toString();
                    pass = rs.getString(5).toString();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_NAME,name);
                    editor.putString(KEY_EMAIL,email);
                    editor.putString(KEY_PASS,pass);
                    editor.putString(KEY_U_ID,u_id);
                    editor.apply();

                    result = "Welcom to the Yo-Travel";
                    Intent intent = new Intent(Login.this, DrawerMain.class);
//                    intent.putExtra("name",name);
//                    intent.putExtra("email",email);
                    startActivity(intent);
                    finish();
                } else {
                    result = "Email or Password Wrong";
                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Login.this, "" + result, Toast.LENGTH_SHORT).show();
//            Name.setText(result);

        }
    }

    private class SCheckLogin extends AsyncTask<String, Void, String> {

        String res = "";




        @Override
        protected String doInBackground(String... params) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Conn.url, Conn.user, Conn.pass);

                String result = "";
                sharedPreferences = getSharedPreferences(SHARED_NAME,MODE_PRIVATE);
                String Email = sharedPreferences.getString(KEY_EMAIL,null);
                String Pass = sharedPreferences.getString(KEY_PASS,null);

//                userName.setText(Email);
//                userPassword.setText(Pass);
//
//                String Email1 = (String) userName.getText().toString();
//                String Pass1 = (String) userPassword.getText().toString();


                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM guider WHERE G_Email = '" + Email + "' AND G_Password ='" + Pass + "'");

                if (rs.next()) {
                    result = "Welcom to the Yo-Travel";
                    Intent intent = new Intent(Login.this, DrawerMain.class);
                    startActivity(intent);
                    finish();
                } else {
                    result = "Email or Password Wrong";
                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Login.this, "" + result, Toast.LENGTH_SHORT).show();
//            Name.setText(result);

        }
    }

    private class CheckLogin2 extends AsyncTask<String, Void, String> {
        String res = "";


        String scaner = (String) QR_Results.getText().toString();


        @Override
        protected String doInBackground(String... params) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Conn.url, Conn.user, Conn.pass);

                String result = "";
                String name = "";
                String email = "";
                String pass = "";
                String u_id = "";

//                SELECT * FROM admin WHERE concat(Email_Address,Password)='$uname' AND A_Status > '$status'
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM guider WHERE concat(G_Email,G_Password) ='" + scaner + "'");
                ResultSetMetaData rsmd = rs.getMetaData();

                if (rs.next()) {

                    u_id = rs.getString(1).toString();
                    name = rs.getString(2).toString();
                    email = rs.getString(4).toString();
                    pass = rs.getString(5).toString();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_NAME,name);
                    editor.putString(KEY_EMAIL,email);
                    editor.putString(KEY_PASS,pass);
                    editor.putString(KEY_U_ID,u_id);
                    editor.apply();

                    result = "Welcom to the Yo-Travel";
                    Intent intent = new Intent(Login.this, DrawerMain.class);
//                    intent.putExtra("name",name);
//                    intent.putExtra("email",email);
                    startActivity(intent);
                    finish();
                } else {
                    result = "Email or Password Wrong";
                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Login.this, "" + result, Toast.LENGTH_SHORT).show();
//            Name.setText(result);

        }
    }
}
