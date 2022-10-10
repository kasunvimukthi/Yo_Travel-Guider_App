package com.yo_travel.yo_travelguider.DrawerActivity2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.yo_travel.yo_travelguider.R;

public class QRcode extends AppCompatActivity {

    String U_Pass, U_Email;

    ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Intent i = getIntent();
        String U_Pass1 = i.getStringExtra("U_Pass");
        String U_Email1 = i.getStringExtra("U_Email");

        U_Pass = U_Pass1;
        U_Email = U_Email1;

        imageView1 = findViewById(R.id.imageViewQR);
        Run run = new Run();
        run.execute();

    }

    private class Run extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap;

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String QR = U_Email+U_Pass;
                MultiFormatWriter writer = new MultiFormatWriter();
                BitMatrix bitMatrix = writer.encode(QR, BarcodeFormat.QR_CODE,500,500);

                BarcodeEncoder encoder = new BarcodeEncoder();
                bitmap = encoder.createBitmap(bitMatrix);

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            imageView1.setImageBitmap(bitmap);
        }
    }

}