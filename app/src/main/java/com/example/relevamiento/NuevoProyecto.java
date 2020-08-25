package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.example.relevamiento.modelos.Proyecto;

import java.io.File;

public class NuevoProyecto extends AppCompatActivity {

    private EditText et_nombre;
    private CheckBox checkBox_fotos;
    private DialogProperties properties;
    private Proyecto proyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_proyecto);

        et_nombre = (EditText) findViewById(R.id.et_nombre);
        checkBox_fotos = (CheckBox) findViewById(R.id.checkBox_fotos);
        properties = new DialogProperties();


    }

    private void cargarDiagrama(View view){
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File("mnt/sdcard/Pictures");
        properties.error_dir = new File("mnt/sdcard/Pictures");
        properties.offset = new File("");
        properties.extensions = null;
        properties.show_hidden_files = false;
        FilePickerDialog dialog = new FilePickerDialog(this,properties);
        dialog.setTitle("Seleccionar Diagrama");
        dialog.setNegativeBtnName("Salir");
        dialog.setPositiveBtnName("Aceptar");
        dialog.show();
       // final Intent intent = new Intent(this, ActivityTouchImage.class);
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
               // String imgPath= null;
                //files is the array of the paths of files selected by the Application User.
                if (files.length > 0){
                   // imgPath = files[0];
                   // intent.putExtra("PathImage", imgPath);
                   // startActivity(intent);
                }
            }

        });

    }

    private void cargarElementos(View view){

    }

    private void abrirProyecto( View view){
        proyecto = new Proyecto(et_nombre.toString(), null, null, checkBox_fotos.isChecked());

    }

}