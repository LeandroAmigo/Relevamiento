package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;

import java.io.File;

public class Documentos extends AppCompatActivity {

    private final String mSampleMimeType = "application/pdf";
    private DialogProperties properties;

    private File mSampleFile;
    private String pathDocumento;

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

        properties = new DialogProperties();

       // abrir dialog picker para seleccionar archivo pdf
        dialogPicker();

    }

    private void dialogPicker() {
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File("mnt/sdcard/Documents");
        properties.error_dir = new File("mnt/sdcard/Documents");
        properties.offset = new File("");
        properties.extensions = null;
        properties.show_hidden_files = false;
        FilePickerDialog dialog = new FilePickerDialog(this, properties);
        dialog.setTitle("Seleccionar Datasheet");
        dialog.setNegativeBtnName("Salir");
        dialog.setPositiveBtnName("Aceptar");
        dialog.show();
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                if (files.length == 1) {
                    pathDocumento = files[0];
                }
            }
        });
    }

    /**
     * Listener for when a the launch document viewer button is clicked
     *
     * @param view The launch launch document viewer button
     */
    public void abrir(View view) {
        if (pathDocumento == null) {
            Toast.makeText(getApplicationContext(),"Error al abrir el documento",Toast.LENGTH_LONG).show();
            return;
        }
        Uri contentUri = Uri.fromFile(new File(pathDocumento));
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

    public void volver (View view){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
/*   NO TESTEADO TODAVIAA
    @Override
    public void onBackPressed (){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

 */


}