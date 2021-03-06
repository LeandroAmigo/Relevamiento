package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;

import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class CrearCargarProyecto extends AppCompatActivity {

    private EditText et_nombre;
    private CheckBox checkBox_fotos;
    private ListView lv_diagramas; //lista de nombres de diagramas
    private TextView tv_elementos; //nombre de archivo de elementos

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

        et_nombre =  findViewById(R.id.et_nombre);
        checkBox_fotos =  findViewById(R.id.checkBox_fotos);
        checkBox_fotos.setChecked(true);
        lv_diagramas =  findViewById(R.id.lv_diagramas);
        tv_elementos =  findViewById(R.id.tv_elemento);

        properties = new DialogProperties();

        repo = new Repositorio(this);

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
            proyectoSeleccionado = repo.getProyecto(nombreProyecto);
            mostrarDatosProyectoPantalla();
        }
    }

    private void mostrarDatosProyectoPantalla() {
        et_nombre.setText(proyectoSeleccionado.getNombre());
        checkBox_fotos.setChecked(proyectoSeleccionado.permite_fotos());
        mostrarNombreDiagramas(proyectoSeleccionado.getDiagramas());
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
                    ArrayList<String> listaDiagramas = new ArrayList<String>(Arrays.asList(pathDiagramas));
                    mostrarNombreDiagramas(listaDiagramas); //actualiza listview
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
                    mostrarNuevosElementosCargados(pathElementos); //actualiza textview
                }
            }
        });
    }

    public void crear_actualizar_Proyecto(View view) {
        boolean exito = false;
        nombreProyecto = et_nombre.getText().toString();
        boolean permiteFoto = checkBox_fotos.isChecked();

        if (proyectoSeleccionado == null){  //crear proyecto
            if (pathDiagramas != null && !nombreProyecto.isEmpty() && !existeNombre(nombreProyecto)) {
                exito = repo.crearProyecto(nombreProyecto, pathDiagramas, pathElementos, permiteFoto); //guarda BD
            }else if (nombreProyecto.isEmpty()) {
                    Toast.makeText(this, "Ingresar nombre ", Toast.LENGTH_SHORT).show();
                }else if (pathDiagramas == null){
                    Toast.makeText(this, "Seleccionar al menos un diagrama", Toast.LENGTH_SHORT).show();
                    }else {
                Toast.makeText(this, "Nombre ya registrado!", Toast.LENGTH_SHORT).show();
            }
        }else { //editar
            int proyId = proyectoSeleccionado.getId();
            if (!nombreProyecto.equals(proyectoSeleccionado.getNombre())) {
                exito = repo.actualizarNombreProyecto(proyId, nombreProyecto); //sobreescribe
            }
            if (pathDiagramas != null) {
                //se agregan los nuevos diagramas de pathDiagramas (se verifica que no esten duplicados)
                exito =  repo.actualizarDiagramasProyecto(proyId, proyectoSeleccionado.getDiagramas(), pathDiagramas); //agrega
            }
            if (permiteFoto != proyectoSeleccionado.permite_fotos()) {
                exito = repo.actualizarPermiteFotosProyecto(proyId, permiteFoto); //sobreescribe
            }
            if (pathElementos != null) {
                repo.agregarElementos(proyId, pathElementos); //agrega
                exito = true;
            }
        }

        if (exito) {
            Toast.makeText(this, "Proyecto guardado", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Principal.class);
            intent.putExtra(Principal.NOMBRE_PROYECTO, nombreProyecto);
            startActivity(intent);
            finish();
        }
    }

    private boolean existeNombre(String nombreProyecto) {
        boolean existe = false;
        ArrayList<String> nombres = repo.getProyectos();
        for (int i=0; i<nombres.size() && !existe; i++){
            existe = nombres.get(i).equals(nombreProyecto);
        }
        return existe;
    }

    private void mostrarNombreDiagramas( ArrayList<String> diagramas){
            ArrayList<String> nombrediagramas = new ArrayList<>();
            for (String s : diagramas) {
                String substring = s.substring(21, s.length() - 4); //elimina ""mnt/sdcard/Pictures/"" y tambien el "".jpg""
                nombrediagramas.add(substring);
            }
            ArrayAdapter<String> arrayadapter = new ArrayAdapter<String>(this, R.layout.tv_listadiagramas_crearcargar, nombrediagramas);
            lv_diagramas.setAdapter(arrayadapter);

    }

    private void mostrarNuevosElementosCargados(String pathElementos) {
        String substring = pathElementos.substring(22, pathElementos.length()-4); //elimina ""mnt/sdcard/Documents/"" y tambien el "".csv""
        tv_elementos.setText(substring);
    }

}