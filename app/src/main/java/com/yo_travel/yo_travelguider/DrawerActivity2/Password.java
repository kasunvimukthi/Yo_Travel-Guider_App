package com.yo_travel.yo_travelguider.DrawerActivity2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yo_travel.yo_travelguider.DB_Connection.Conn;
import com.yo_travel.yo_travelguider.DrawerMain.DrawerMain;
import com.yo_travel.yo_travelguider.R;
import com.yo_travel.yo_travelguider.md5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Password extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private static final String SHARED_NAME = "my";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASS = "Pass";
    private static final String KEY_U_ID = "";

    TextView textView1, textView2, textView3;

    Button button1;

    String U_ID, U_Email;

    Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Intent i = getIntent();
        String U_ID1 = i.getStringExtra("U_ID");
        String U_Email1 = i.getStringExtra("U_Email");

        U_ID = U_ID1;
        U_Email = U_Email1;

        dialog1 = new Dialog(Password.this);

        textView1 = findViewById(R.id.NewPass);
        textView2 = findViewById(R.id.CurrentPass);
        textView3 = findViewById(R.id.ConfirmPass);

        button1 = findViewById(R.id.button6);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String curr = textView2.getText().toString();
                String new1 = textView1.getText().toString();
                String conf = textView3.getText().toString();

                if (curr.isEmpty()){
                    Toast.makeText(Password.this, "Please Enter Your Current Password", Toast.LENGTH_SHORT).show();
                }else
                    if (new1.isEmpty()){
                    Toast.makeText(Password.this, "Please Enter Your New Password", Toast.LENGTH_SHORT).show();
                }else
                    if (new1.length() < 5){
                    Toast.makeText(Password.this, "Password Should Be More Than 5 Characters", Toast.LENGTH_SHORT).show();
                }else
                    if (conf.isEmpty()){
                    Toast.makeText(Password.this, "Please Re-Enter Your New Password", Toast.LENGTH_SHORT).show();
                }else
                    if (!new1.equals(conf)){
                    Toast.makeText(Password.this, "Confirmation Password Not Match!", Toast.LENGTH_SHORT).show();
                }else{
                    Getdata1 getdata1 = new Getdata1();
                    getdata1.execute();
                }
            }
        });
    }

    private class Getdata1 extends AsyncTask<Void, Void, String> {

        String res = "";

        md5 encryption = new md5(textView2.getText().toString());
        String pass = encryption.getEncryption();

        md5 encryption1 = new md5(textView1.getText().toString());
        String pass1 = encryption1.getEncryption();

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Conn.url, Conn.user, Conn.pass);

//                String Name = textView1.getText().toString();

                String result = "";

                sharedPreferences = getSharedPreferences(SHARED_NAME,MODE_PRIVATE);

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM guider WHERE G_Email = '" + U_Email + "' AND G_Password ='" + pass + "'");

                if (rs.next()) {
                    String query = "UPDATE guider SET G_Password = '"+ pass1 +"' WHERE ID = '" + U_ID + "'";
                    Statement st1 = con.createStatement();
                    int rs1 = st1.executeUpdate(query);

                    if (rs1 > 0) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String name = null;
                        editor.putString(KEY_PASS,name);
                        editor.commit();

                        SharedPreferences.Editor editor2 = sharedPreferences.edit();
                        editor2.putString(KEY_PASS,pass1);
                        editor2.apply();

                        result = "1";

                    }else{
                        result = "2";
                    }

                }else {
                    result = "3";
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
            dialog1.setContentView(R.layout.dialog_box);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView title = dialog1.findViewById(R.id.DialogTitle);
            TextView info = dialog1.findViewById(R.id.DialogInfo);
            Button button = dialog1.findViewById(R.id.DialogButton);
            ImageView imageView = dialog1.findViewById(R.id.DialogImageView);

            if (result.equals("1")){
                imageView.setImageResource(R.drawable.ic_done_green);
                imageView.setBackgroundResource(R.drawable.green_sircle);
                title.setText("Done");
                title.setTextColor(Color.parseColor("#348D1A"));
                info.setText("Your Password Updated Now");
                button.setText("OK");
                button.setBackgroundColor(Color.parseColor("#348D1A"));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Password.this, DrawerMain.class);
                        startActivity(intent);
                        finish();
                    }
                });

                dialog1.setCanceledOnTouchOutside(false);
                dialog1.show();

            }else if (result.equals("2")){
                imageView.setImageResource(R.drawable.ic_warning);
//                imageView.setBackgroundResource(R.drawable.red_sircle);
                title.setText("Warning");
                title.setTextColor(Color.parseColor("#EFC90B"));
                info.setText("Something Went Wrong!");
                button.setText("OK");
                button.setBackgroundColor(Color.parseColor("#EFC90B"));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
            }else if (result.equals("3")){
                imageView.setImageResource(R.drawable.ic_close_red);
                imageView.setBackgroundResource(R.drawable.red_sircle);
                title.setText("Error");
                title.setTextColor(Color.parseColor("#EF0B0B"));
                info.setText("Your Current Password Is Wrong!");
                button.setText("OK");
                button.setBackgroundColor(Color.parseColor("#EF0B0B"));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
            }
        }

    }

}