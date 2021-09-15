package com.example.aashish.shopmobile.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aashish.shopmobile.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class BarGeneratorActivity extends AppCompatActivity {

    Button btnCreate, btnadd;
    ImageView imgCode;
    EditText txtCode;
    TextView txtBC;

    String txtC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_generator);
        btnCreate = findViewById(R.id.btnCode);
        btnadd = findViewById(R.id.btnAdd);
        imgCode = findViewById(R.id.imgCode);
        txtCode = findViewById(R.id.txtBarcode);
        txtBC = findViewById(R.id.txtBC);
        btnadd.setVisibility(View.INVISIBLE);


      //  txtCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)};



            btnCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtC = txtCode.getText().toString().trim();

                    if (txtC.isEmpty()) {
                        Toast.makeText(BarGeneratorActivity.this, "Please Enter a Barcode", Toast.LENGTH_SHORT).show();
                    }
                    else if(txtC.length() != 12 )
                    {
                        Toast.makeText(BarGeneratorActivity.this, "Please Enter a 12 digit for a barcode", Toast.LENGTH_SHORT).show();

                    }
                    else {


                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(txtC, BarcodeFormat.ITF, 400, 200);
                            //int width = bitMatrix.getWidth();
                           // int height = bitMatrix.getHeight();
                            //Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                           // for (int x = 0; x < width; x++) {
                               // for (int y = 0; y < height; y++) {
                                 //   bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                              //  }
                           // }

                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            imgCode.setImageBitmap(bitmap);
                            txtBC.setText(txtC);
                            btnadd.setVisibility(View.VISIBLE);

                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                    }
                }


            });

            btnadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent in = new Intent(BarGeneratorActivity.this, HomeActivity.class);
                        in.putExtra("txtC",txtC);
                        startActivity(in);

                }
            });








    }
    }
