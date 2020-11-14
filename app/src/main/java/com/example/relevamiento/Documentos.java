package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;

import java.io.File;

public class Documentos extends AppCompatActivity {

    private final String mSampleMimeType = "application/pdf";
    private DialogProperties properties;
    private TextView tv_nombreDocumento;

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
        tv_nombreDocumento = findViewById(R.id.tv_nombreDoc);

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
                    mostrarNombreDocumentoCargado(pathDocumento);
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

        File document = new File(pathDocumento);
        final Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", document);

        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                MimeTypeMap.getFileExtensionFromUrl(document.toURI().toString()));

        if(mimeType != null){
            if( !mimeType.equals(mSampleMimeType) ){
                Toast.makeText(getApplicationContext(),"el formato debe ser PDF (xxx.pdf)",Toast.LENGTH_LONG).show();
            }else{
                final Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                viewIntent.addCategory(Intent.CATEGORY_DEFAULT);
                viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                viewIntent.setDataAndType(contentUri, mimeType);
                viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(viewIntent);
            }
        }
    }

    public void volver (View view){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }


    public void seleccionar( View view){
        dialogPicker();
    }

    private void mostrarNombreDocumentoCargado(String pathDocumento) {
        String substring = pathDocumento.substring(22, pathDocumento.length()-4); //elimina ""mnt/sdcard/Documents/"" y tambien el "".pdf""
        tv_nombreDocumento.setText(substring);
    }



}