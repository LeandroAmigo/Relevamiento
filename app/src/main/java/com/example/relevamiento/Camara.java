package com.example.relevamiento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class Camara extends AppCompatActivity {

    // Request code identifying camera events
    private static final int BASIC_CAMERA_REQUEST_CODE = 1889;
    private static final int FILEPROVIDER_CAMERA_REQUEST_CODE = 1998;

    // Identifier for the image returned by the camera
    private static final String EXTRA_RESULT = "data";

    private ImageView mImageView;
    private Button btn_aceptar;
    private Uri contentUri;
    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camara);

        mImageView = (ImageView) findViewById(R.id.camera_image_view);
        btn_aceptar = findViewById(R.id.btn_aceptarCamara);
        btn_aceptar.setEnabled(false);
    }

    /**
     * Listener for when the basic camera button is clicked
     *
     * @param view The launch camera button
     */
    public void onLaunchCameraBasic(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // OR ACTION_VIDEO_CAPTURE
        startActivityForResult(intent, BASIC_CAMERA_REQUEST_CODE);
    }

    /**
     * Listener for when the FileProvider camera button is clicked
     *
     * @param view The FileProvider camera button
     */
   /* public void onLaunchCameraFileProvider(View view) {

        final File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);

        final File file = new File(mediaStorageDir, "devexamples-" + UUID.randomUUID() + ".jpg");

        contentUri = FileProvider.getUriForFile(
                getApplicationContext(),
                getApplicationContext().getPackageName() + ".fileprovider",
                file);

        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // OR ACTION_VIDEO_CAPTURE
        captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        startActivityForResult(captureIntent, FILEPROVIDER_CAMERA_REQUEST_CODE);
    }*/

    /**
     * Listener for result from external activities. Receives image data from camera.
     *
     * @param requestCode See Android docs
     * @param resultCode  See Android docs
     * @param data        See Android docs
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case BASIC_CAMERA_REQUEST_CODE: // Display Bitmap received from Camera
                    Bitmap photo = data.getExtras().getParcelable(EXTRA_RESULT);
                    mImageView.setImageBitmap(photo);

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    path = getRealPathFromURI(tempUri);
                    File finalFile = new File(path);

                    btn_aceptar.setEnabled(true);
                    break;

               /* case FILEPROVIDER_CAMERA_REQUEST_CODE: // View saved file in DocumentViewer

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(contentUri, "image/*");
                    intent.putExtra("zoom", "2");

                    startActivityForResult(intent, 1234);
                    break;*/
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public void aceptar (View view){
        Intent i = new Intent();
        i.putExtra(Planilla.FOTO, path);
        setResult(Activity.RESULT_OK,i);
        finish();
    }



}