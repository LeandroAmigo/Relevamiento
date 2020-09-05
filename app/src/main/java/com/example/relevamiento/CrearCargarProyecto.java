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

import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.parsers.String_ListaStringNombreElementos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CrearCargarProyecto extends AppCompatActivity {

    private EditText et_nombre;
    private CheckBox checkBox_fotos;
    private DialogProperties properties;
    Proyecto proyecto;
    private String[] pathDiagramas;
    private String pathElementos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cargar_proyecto);

        et_nombre = (EditText) findViewById(R.id.et_nombre);
        checkBox_fotos = (CheckBox) findViewById(R.id.checkBox_fotos);

        properties = new DialogProperties();
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
                    pathDiagramas = files;
                }
            }
        });
    }

    public void cargarListaElementos(View view){
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File("mnt/sdcard/Documents");
        properties.error_dir = new File("mnt/sdcard/Documents");
        properties.offset = new File("");
        properties.extensions = null;
        properties.show_hidden_files = false;
        FilePickerDialog dialog = new FilePickerDialog(this,properties);
        dialog.setTitle("Seleccionar Lista de Elementos");
        dialog.setNegativeBtnName("Salir");
        dialog.setPositiveBtnName("Aceptar");
        dialog.show();
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                if (files.length == 1){
                    pathElementos = files[0];
                }
            }
        });
    }




    public void crearProyecto(){

        //buscar en BD si existia proyecto con mismo nombre y traer sus cosas guardadas
        //sino:

        String nombreProyecto = et_nombre.getText().toString();
        if (nombreProyecto.isEmpty() ){
            Toast.makeText(this, "Ingresar nombre de Proyecto", Toast.LENGTH_SHORT).show();
        }
        boolean permiteFoto = checkBox_fotos.isChecked();
        ArrayList<String> listaDiagramas = (ArrayList<String>) Arrays.asList(pathDiagramas);
        proyecto = new Proyecto(nombreProyecto, listaDiagramas, permiteFoto);

        if (pathElementos != null) {
            ArrayList<String> listaNombreElementos = String_ListaStringNombreElementos.obtenerListaElementos(pathElementos);
            ArrayList<Elemento> listaElementos = proyecto.getElementos();
            for ( String s: listaNombreElementos) {
                listaElementos.add (new Elemento(s));
            }
        }
        guardarProyectoBD(proyecto);
    }

    private void guardarProyectoBD(Proyecto proyecto){


    }

}