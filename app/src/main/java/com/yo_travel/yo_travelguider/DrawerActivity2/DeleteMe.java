package com.yo_travel.yo_travelguider.DrawerActivity2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yo_travel.yo_travelguider.DB_Connection.Conn;
import com.yo_travel.yo_travelguider.DrawerMain.DrawerMain;
import com.yo_travel.yo_travelguider.MainActivity;
import com.yo_travel.yo_travelguider.R;
import com.yo_travel.yo_travelguider.md5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DeleteMe extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private static final String SHARED_NAME = "my";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASS = "Pass";
    private static final String KEY_U_ID = "";

    String U_ID, U_Email;

    TextView textView1;

    Button delete;

    CheckBox checkBox1;

    Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_me);

        Intent i = getIntent();
        String U_ID1 = i.getStringExtra("U_ID");
        String U_Email1 = i.getStringExtra("U_Email");

        dialog1 = new Dialog(DeleteMe.this);

        U_ID = U_ID1;
        U_Email = U_Email1;

        textView1 = findViewById(R.id.DeletePass);
        checkBox1 = findViewById(R.id.checkBoxDelete);
        delete = findViewById(R.id.button7Delete);

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox1.isChecked()){
                    delete.setEnabled(true);
                }else{
                    delete.setEnabled(false);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pass1 = textView1.getText().toString();

                if (Pass1.isEmpty()){
                    Toast.makeText(DeleteMe.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                }else {
                    Delete delete = new Delete();
                    delete.execute();
                }
            }
        });
    }

    private class Delete extends AsyncTask<String, Void, String> {

        String res = "";
        md5 encryption = new md5(textView1.getText().toString());
        String pass = encryption.getEncryption();

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(Conn.url, Conn.user, Conn.pass);

                String result = "";

                sharedPreferences = getSharedPreferences(SHARED_NAME,MODE_PRIVATE);
                String ID = sharedPreferences.getString(KEY_U_ID,null);

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM guider WHERE G_Email = '" + U_Email + "' AND G_Password ='" + pass + "'");

                if (rs.next()) {


                    String query = "DELETE FROM `guider` WHERE `ID` = '"+ID+"'";
                    Statement st1 = con.createStatement();
                    int rs1 = st1.executeUpdate(query);

                    if (rs1 > 0) {
                        Intent intent = new Intent(DeleteMe.this, DrawerMain.class);
                        startActivity(intent);
                        finish();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String name = null;
                        editor.putString(KEY_NAME,name);
                        editor.putString(KEY_EMAIL,name);
                        editor.putString(KEY_U_ID,name);
                        editor.commit();

                        result = "1";

                    }else{
                        result = "2";
                    }
                }else
                {
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
                info.setText("Your Account Delete Now");
                button.setText("OK");
                button.setBackgroundColor(Color.parseColor("#348D1A"));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DeleteMe.this, MainActivity.class);
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

}