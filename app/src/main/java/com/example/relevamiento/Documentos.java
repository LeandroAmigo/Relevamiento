package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

public class Documentos extends AppCompatActivity {

    private final String mSampleFileName = "sample.pdf";
    private final String mSampleFolderName = "Documents";
    private final String mSampleMimeType = "application/pdf";

    private File mSampleFile;

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState See Android docs
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_documentos);

       /* try {
            mSampleFile = Utils.copyFromAssetsToExternal(this, mSampleFileName, mSampleFolderName);
        } catch (IOException ex) {
            Toast.makeText(this, "Failed to copy sample file", Toast.LENGTH_LONG).show();
        }*/
    }

    /**
     * Listener for when a the launch document viewer button is clicked
     *
     * @param view The launch launch document viewer button
     */
    public void onLaunchDocument(View view) {
        final Uri contentUri = FileProvider.getUriForFile(
                getApplicationContext(),
                getApplicationContext().getPackageName() + ".fileprovider",
                mSampleFile);

        if (mSampleFile == null) {
            Toast.makeText(getApplicationContext(),"Failed to find sample file",Toast.LENGTH_LONG).show();
            return;
        }

        final Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.addCategory(Intent.CATEGORY_DEFAULT);
        viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        viewIntent.setDataAndType(contentUri, mSampleMimeType);
        viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        // Optionally can control visual appearance
        //
        viewIntent.putExtra("page", "1"); // Open a specific page
        viewIntent.putExtra("zoom", "2"); // Open at a specific zoom level

        startActivity(viewIntent);
    }


}