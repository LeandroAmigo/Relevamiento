package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;

import java.io.File;

public class CrearCargarProyecto extends AppCompatActivity {

    private EditText et_nombre;
    private CheckBox checkBox_fotos;
    private DialogProperties properties;
    private String[] pathDiagramas = null;
    private String pathElementos = null, nombreProyecto = null;
    private Repositorio repo;
    private Proyecto proyectoSeleccionado;

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
            proyectoSeleccionado = getProyecto(nombreProyecto);
            mostrarDatosProyectoPantalla();
            Log.e("CREAR CARGAR ONCREATE", "");
        }
    }

    private void mostrarDatosProyectoPantalla() {
        et_nombre.setText(proyectoSeleccionado.getNombre());
        checkBox_fotos.setChecked(proyectoSeleccionado.permite_fotos());
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

    private Proyecto getProyecto(String nombreProyecto){
        return repo.getProyecto(nombreProyecto);
    }

    public void crear_actualizar_Proyecto(View view) {
        boolean exito = false;
        nombreProyecto = et_nombre.getText().toString(); /////CONTROLAR Q NO SEA VACIO ///// idem para DIAGRAMAS
        boolean permiteFoto = checkBox_fotos.isChecked();

        if (proyectoSeleccionado == null){  //crear proyecto
            if (pathDiagramas == null) {
                Toast.makeText(this, "Seleccionar al menos un diagrama", Toast.LENGTH_SHORT).show();
            }else {
                Log.e("CREAR PROY", nombreProyecto + " - " + pathDiagramas + " - " + pathElementos + " - " + permiteFoto);
                exito = repo.crearProyecto(nombreProyecto, pathDiagramas, pathElementos, permiteFoto); //guarda BD
            }
        }else {
            int proyId = proyectoSeleccionado.getId();
            if (!nombreProyecto.equals(proyectoSeleccionado.getNombre())) {
                Log.e("EDITANDO NOMBRE", nombreProyecto + " -- " + proyectoSeleccionado.getNombre());
                exito = actualizarNombreProyecto(proyId, nombreProyecto); //sobreescribe
            }
            if (pathDiagramas != null) {
                Log.e("EDITANDO DIAGRAMAS", pathDiagramas + " -- " + proyectoSeleccionado.getDiagramas());
                exito = actualizarDiagramasProyecto(proyId, pathDiagramas); //sobreescribe
            }
            if (permiteFoto != proyectoSeleccionado.permite_fotos()) {
                exito = actualizarPermiteFotosProyecto(proyId, permiteFoto); //sobreescribe
            }
            if (pathElementos != null) {
                agregarElementos(proyId, pathElementos); //agrega
            }
        }

        if (exito == true) {
            Toast.makeText(this, "Proyecto guardado", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Principal.class);
            intent.putExtra(Principal.NOMBRE_PROYECTO, nombreProyecto);
            startActivity(intent);
        }
    }

    private void agregarElementos(int proyId, String pathElementos) {
         repo.agregarElementos(proyId, pathElementos);
    }

    private boolean actualizarPermiteFotosProyecto(int proyId, boolean permiteFoto) {
        return repo.actualizarPermiteFotosProyecto(proyId, permiteFoto);
    }

    private boolean actualizarDiagramasProyecto(int proyId, String[] pathDiagramas) {
        return repo.actualizarDiagramasProyecto(proyId, pathDiagramas);
    }

    private boolean actualizarNombreProyecto(int proyId, String nombreProyecto) {
        return repo.actualizarNombreProyecto(proyId, nombreProyecto);
    }


}