package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;

import com.example.relevamiento.repositorio.Repositorio;

import java.io.File;

public class CrearCargarProyecto extends AppCompatActivity {

    private EditText et_nombre;
    private CheckBox checkBox_fotos;
    private DialogProperties properties;
    private String[] pathDiagramas;
    private String pathElementos, nombreProyecto;
    private int proyId = -1;
    private Repositorio repo;

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cargar_proyecto);

        et_nombre = (EditText) findViewById(R.id.et_nombre);
        checkBox_fotos = (CheckBox) findViewById(R.id.checkBox_fotos);

        properties = new DialogProperties();

        repo = new Repositorio(this);

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
            et_nombre.setText(nombreProyecto);
            proyId = getIdProyecto(nombreProyecto);
        }

    }

    //setea variable pathDiagramas usando FilePicker
    public void cargarDiagramas(View view) {
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File("mnt/sdcard/Pictures");
        properties.error_dir = new File("mnt/sdcard/Pictures");
        properties.offset = new File("");
        properties.extensions = null;
        properties.show_hidden_files = false;
        FilePickerDialog dialog = new FilePickerDialog(this, properties);
        dialog.setTitle("Seleccionar Diagramas");
        dialog.setNegativeBtnName("Salir");
        dialog.setPositiveBtnName("Aceptar");
        dialog.show();
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                if (files.length > 0) {
                    pathDiagramas = files;
                }
            }
        });
    }

    //setea variable pathElementos usando FilePicker
    public void cargarListaElementos(View view) {
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File("mnt/sdcard/Documents");
        properties.error_dir = new File("mnt/sdcard/Documents");
        properties.offset = new File("");
        properties.extensions = null;
        properties.show_hidden_files = false;
        FilePickerDialog dialog = new FilePickerDialog(this, properties);
        dialog.setTitle("Seleccionar Lista de Elementos");
        dialog.setNegativeBtnName("Salir");
        dialog.setPositiveBtnName("Aceptar");
        dialog.show();
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                if (files.length == 1) {
                    pathElementos = files[0];
                }
            }
        });
    }

    private int getIdProyecto(String nombreProyecto) {
        return repo.getIdProyecto(nombreProyecto);
    }

    public void crear_actualizar_Proyecto(View view) {
        boolean exito;
        nombreProyecto = et_nombre.getText().toString(); /////CONTROLAR Q NO SEA VACIO
        boolean permiteFoto = checkBox_fotos.isChecked();
        Log.e("BOOLEAN", ""+permiteFoto);

        if (proyId == -1) {
            exito = repo.crearProyecto(nombreProyecto, pathDiagramas, pathElementos, permiteFoto); //guarda BD
        }else{
            exito = repo.actualizarProyecto(proyId, nombreProyecto, pathDiagramas, pathElementos, permiteFoto);//actualiza BD
        }

        if (exito == true)
            Toast.makeText(this, "Proyecto guardado", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "se produjo un ERROR!", Toast.LENGTH_SHORT).show();
    }

}