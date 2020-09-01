package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.example.relevamiento.modelos.Proyecto;

import java.io.File;
import java.nio.file.Path;

public class CrearCargarProyecto extends AppCompatActivity {

    private EditText nombreProyecto;
    private CheckBox checkBox_fotos;
    private DialogProperties properties;
    Proyecto proyecto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cargar_proyecto);

        nombreProyecto = (EditText) findViewById(R.id.et_nombre);
        checkBox_fotos = (CheckBox) findViewById(R.id.checkBox_fotos);

        properties = new DialogProperties();
        SQLiteDatabase BaseDeDatos = MainActivity.getBD().getWritableDatabase();
        proyecto = new Proyecto();

    }



    public void cargarDiagramas(View view){
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File("mnt/sdcard/Pictures");
        properties.error_dir = new File("mnt/sdcard/Pictures");
        properties.offset = new File("");
        properties.extensions = null;
        properties.show_hidden_files = false;
        FilePickerDialog dialog = new FilePickerDialog(this,properties);
        dialog.setTitle("Seleccionar Diagramas");
        dialog.setNegativeBtnName("Salir");
        dialog.setPositiveBtnName("Aceptar");
        dialog.show();
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                if (files.length > 0){
                    for (String s:files) {
                        Log.e("PATH", s);
                    }
                     //proyecto.setDiagramas(files);

                }
            }
        });
    }

}