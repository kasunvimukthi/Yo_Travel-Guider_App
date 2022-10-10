package com.yo_travel.yo_travelguider.DrawerActivity2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
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

public class My_Info extends AppCompatActivity {

    TextView textView1, textView2, textView3, textView4, textView5, textView6;

    Button button1;

    String Name, Email, Age, Address, Contact, Pass, U_ID, U_Email;

    Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        Getdata2 getdata2 = new Getdata2();
        getdata2.execute();

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.ConfirmPass);

        button1 = findViewById(R.id.button5);

        dialog1 = new Dialog(My_Info.this);

        Intent i = getIntent();
        String U_ID1 = i.getStringExtra("U_ID");
        String U_Email1 = i.getStringExtra("U_Email");

        U_ID = U_ID1;
        U_Email = U_Email1;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = (String) textView1.getText().toString();
                String email = (String) textView2.getText().toString();
                String age = (String) textView3.getText().toString();
                String address = (String) textView4.getText().toString();
                String contact = (String) textView5.getText().toString();
                String pass = (String) textView6.getText().toString();

                if (name.isEmpty()){
                    Toast.makeText(My_Info.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                }else if (email.isEmpty()){
                    Toast.makeText(My_Info.this, "Please Enter Your Email Address", Toast.LENGTH_SHORT).show();
                }else if (age.isEmpty()){
                    Toast.makeText(My_Info.this, "please Enter Your Age", Toast.LENGTH_SHORT).show();
                }else if (address.isEmpty()){
                    Toast.makeText(My_Info.this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
                }else if (contact.isEmpty()){
                    Toast.makeText(My_Info.this, "Please Enter Your Contact Number", Toast.LENGTH_SHORT).show();
                }else if (contact.length() != 10){
                    Toast.makeText(My_Info.this, "Please Check Your Contact Number", Toast.LENGTH_SHORT).show();
                }else if (pass.isEmpty()){
                    Toast.makeText(My_Info.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                }else {

                    Getdata1 getdata1 = new Getdata1();
                    getdata1.execute();

                }
            }
        });
    }

    private class Getdata1 extends AsyncTask<Void, Void, String> {

        String res = "";

        md5 encryption = new md5(textView6.getText().toString());
        String pass = encryption.getEncryption();

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Conn.url, Conn.user, Conn.pass);

                String Name = textView1.getText().toString();
                String Email = textView2.getText().toString();
                String Age = textView3.getText().toString();
                String Address = textView4.getText().toString();
                String Contact = textView5.getText().toString();
//              String Pass = textView6.getText().toString();

                String result = "";

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM guider WHERE G_Email = '" + U_Email + "' AND G_Password ='" + pass + "'");

                if (rs.next()) {
                    String query = "UPDATE guider SET G_Name = '"+ Name +"' , G_Contact_No = '"+Contact+"' , Address = '"+Address+"' , Age = '"+Age+"' WHERE ID = '" + U_ID + "'";
                    Statement st1 = con.createStatement();
                    int rs1 = st1.executeUpdate(query);

                    if (rs1 > 0) {
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
                info.setText("Your Details Updated Now");
                button.setText("OK");
                button.setBackgroundColor(Color.parseColor("#348D1A"));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(My_Info.this, DrawerMain.class);
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
                info.setText("Your Entered Password Is Wrong!");
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

    private class Getdata2 extends AsyncTask<Void, Void, Void> {

        String res = "";
        String pass = "";
        String name = "";
        String email = "";
        String age = "";
        String address = "";
        String conatct = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Conn.url, Conn.user, Conn.pass);

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM `guider` WHERE ID = " + U_ID + "");

                while (rs.next()) {
                    name = rs.getString(2).toString();
                    email = rs.getString(4).toString();
                    age = rs.getString(9).toString();
                    address = rs.getString(8).toString();
                    conatct = rs.getString(3).toString();
                }

            }catch (Exception e){
                res = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            textView1.setText(name);
            textView2.setText(email);
            textView3.setText(age);
            textView4.setText(address);
            textView5.setText(conatct);
        }
    }

}