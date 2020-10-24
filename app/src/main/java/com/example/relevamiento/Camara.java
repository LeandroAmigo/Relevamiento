package com.example.relevamiento;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Camara extends AppCompatActivity {

    private static final String IMAGE_DIRECTORY_NAME = "FOTOS RELEVAMIENTO" ;
    File photoFile = null;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    String path;
    private ImageView mImageView;
    private Button btn_foto, btn_aceptar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camara);

        mImageView = (ImageView) findViewById(R.id.imageView);
        btn_foto = findViewById(R.id.btnCaptureImage);
        btn_aceptar = findViewById(R.id.btn_aceptarFoto);
        btn_aceptar.setEnabled(false);

        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    captureImage2();
            }
        });

    }
    /* Capture Image function for 4.4.4 and lower. Not tested for Android Version 3 and 2 */
    private void captureImage2() {

        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = createImageFile4();
            if(photoFile!=null) {
               // displayMessage(getBaseContext(),photoFile.getAbsolutePath());
               // Log.i("Mayank",photoFile.getAbsolutePath());
                Uri photoURI  = Uri.fromFile(photoFile);
                path = photoFile.toString();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
        catch (Exception e)
        {
            displayMessage(getBaseContext(),"Camera is not available."+e.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Bundle extras = data.getExtras();
        //Bitmap imageBitmap = (Bitmap) extras.get("data");
        //imageView.setImageBitmap(imageBitmap);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            mImageView.setImageBitmap(myBitmap);
            btn_aceptar.setEnabled(true);
        } else {
            displayMessage(getBaseContext(), "Request cancelled or something went wrong.");
        }
    }

    private File createImageFile4()
    {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                displayMessage(getBaseContext(),"Unable to create directory.");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;

    }

    private void displayMessage(Context context, String message) {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
    }

    public void aceptar (View view){
        Intent i = new Intent();
        i.putExtra(Planilla.FOTO, path);
        setResult(Activity.RESULT_OK,i);
        finish();
    }

}