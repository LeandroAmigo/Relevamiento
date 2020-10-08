package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.repositorio.Repositorio;

import java.io.File;
import java.util.ArrayList;

public class Planilla extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";
    public static final String DIAGRAMA = "diagrama";
    public static final String MARCAS = "marcas";
    public static final String SWITCH = "switch";

    public static final String AUDIO = "audio";
    public static final String FOTO = "foto";

    private static final int ACTIVITY_GRABAR_AUDIO = 1;
    private static final int BASIC_CAMERA_REQUEST_CODE = 1889;
    private static final int CAMERA_REQUEST_URI = 1888;
    private static final int ACTIVITY_PLANO = 33;


    private Repositorio repo;
    private int proyId;
    private ArrayList<String> elem_seleccionados, nombreElementos;
    private ArrayAdapter<String> adapter, adapterSeleccion;
    private String pathAudio, pathFoto, nombreProyecto, diagramaActual;

    private AutoCompleteTextView atv_nombreElem;
    private ListView elementosSeleccionados;
    private ArrayList<Integer> listaMarcas;
    private boolean correcto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planilla);

        atv_nombreElem = (AutoCompleteTextView) findViewById(R.id.atv_nombreElementos);
        elementosSeleccionados = (ListView) findViewById(R.id.lv_listaElem);

        nombreElementos = new ArrayList<String>();
        listaMarcas = new ArrayList<Integer>();

        repo = new Repositorio(this);
        //inicializa lista
        elem_seleccionados = new ArrayList<String>();
        adapterSeleccion = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, elem_seleccionados);

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
            proyId = repo.getIdProyecto(nombreProyecto);
        }
        if (getIntent().hasExtra(DIAGRAMA)) {
            diagramaActual = getIntent().getStringExtra(DIAGRAMA);
        }

        mostrarListaElementos();

        Intent intent = new Intent("com.realwear.wearhf.intent.action.MOUSE_COMMANDS");
        intent.putExtra("com.realwear.wearhf.intent.extra.MOUSE_ENABLED", false);
        sendBroadcast(intent);

        atv_nombreElem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private void mostrarListaElementos() {
        ArrayList<Elemento> listaElementos = repo.getElementos(proyId);
        for (Elemento e: listaElementos) {
            nombreElementos.add(e.getNombre());
        }
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, nombreElementos);
        atv_nombreElem.setAdapter(adapter);
    }

    public void agregarElementos(View view){
        String elem = atv_nombreElem.getText().toString();
        if (elem.isEmpty()) {
            Toast.makeText(this, "Ingresar Elemento relevado", Toast.LENGTH_SHORT).show();
        }else {
            atv_nombreElem.setText("");
            if ( !nombreElementos.contains(elem)){
                repo.agregarNuevoElemento(proyId, elem);
            }
            elem_seleccionados.add(elem); //lista de elementos relevados
            elementosSeleccionados.setAdapter(adapterSeleccion); //mostrarlos
        }
    }

    public void agregarAudio(View view){
        Intent i = new Intent(this, GrabadoraAudio.class);
        startActivityForResult(i, ACTIVITY_GRABAR_AUDIO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case ACTIVITY_GRABAR_AUDIO:
                    pathAudio = data.getStringExtra(AUDIO);
                    break;

                case ACTIVITY_PLANO:
                    listaMarcas = data.getIntegerArrayListExtra(MARCAS);
                    correcto = data.getBooleanExtra(SWITCH, true);
                    break;

                case CAMERA_REQUEST_URI:
                    //Bitmap photo = data.getExtras().getParcelable(EXTRA_RESULT);
                    //mImageView.setImageBitmap(photo);
                    break;
            }
        }
    }

    public void agregarFoto(View view) {
     Intent i = new Intent(this, Camara.class);
     startActivity(i);
    }

    public void verDocumento(View view) {
        Intent i = new Intent(this, Documentos.class);
        startActivity(i);
    }

    public void verPlano(View view) {
        Intent i = new Intent(this, Marcar.class);
        i.putExtra(Marcar.DIAGRAMA, diagramaActual);
        i.putExtra(Marcar.MARCAS, listaMarcas);
        i.putExtra(Marcar.SWITCH, correcto);
        startActivityForResult(i, ACTIVITY_PLANO);
    }



    public void guardarFormulario(View view){
        if (listaMarcas.size() < 4) {
            Toast.makeText(this, "Marcar region relevada", Toast.LENGTH_SHORT).show();
        }else{  //crear Formulario en BD
            boolean exito;
            int formId = repo.crearFormulario(proyId, diagramaActual, listaMarcas, correcto);

            if (formId != -1){ //correcto
                exito = actualizarElementos(formId);

                restaurarPreferencias();
                Intent i = new Intent(this, Principal.class);
                i.putExtra(Planilla.NOMBRE_PROYECTO, nombreProyecto);
                i.putExtra(Planilla.DIAGRAMA, diagramaActual);
                startActivity(i);
                finish();
            }
            /////solo guarda AUDIO para probar
           // exito = repo.agregarDatosFormulario(formId, pathAudio);

        }
    }

    private boolean actualizarElementos(int formId) {
        boolean exito= false;
        for (String s: elem_seleccionados) {
            int elemId = repo.getIdElemento(s, proyId);
            exito = repo.actualizarElemento(elemId, formId);
            Log.e("EXITO ACTULIZAR ELEM", ""+exito);
        }
        return exito;
    }

    private void restaurarPreferencias(){ // el boton guardar lo invoca!!!
        SharedPreferences sp = getSharedPreferences("coordenadas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }

}