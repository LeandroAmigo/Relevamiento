package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.modelos.Formulario;
import com.example.relevamiento.repositorio.Repositorio;
import com.example.relevamiento.repositorio.parsers.MyAdapter;
import com.example.relevamiento.repositorio.parsers.StatusAdapter;

import java.util.ArrayList;

public class Planilla extends AppCompatActivity {

    public static final String ID_PROYECTO = "proyecto_id";
    public static final String ID_FORMULARIO = "formulario_id";
    public static final String NOMBRE_PROYECTO = "proyecto_nombre";
    public static final String DIAGRAMA = "diagrama";

    public static final String AUDIO = "audio";
    public static final String FOTO = "foto";

    private static final int ACTIVITY_GRABAR_AUDIO = 1;
    private static final int ACTIVITY_FOTO = 2;
    private static final int ACTIVITY_PLANO = 3;


    private Repositorio repo;
    private int proyId, formId;
    private ArrayList<String> elem_seleccionados;
    private ArrayAdapter<String> adapter, adapterSeleccion;
    private String pathAudio, pathFoto;

 //   private Spinner spinner;
    private AutoCompleteTextView atv_nombreElem;
    private ListView elementosSeleccionados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planilla);

        atv_nombreElem = (AutoCompleteTextView) findViewById(R.id.atv_nombreElementos);
        elementosSeleccionados = (ListView) findViewById(R.id.lv_listaElem);

        repo = new Repositorio(this);
        //inicializa lista
        elem_seleccionados = new ArrayList<String>();
        adapterSeleccion = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, elem_seleccionados);

        if (getIntent().hasExtra(ID_PROYECTO) && getIntent().hasExtra(ID_FORMULARIO)) {
            proyId = getIntent().getIntExtra(ID_PROYECTO,-1);
            formId = getIntent().getIntExtra(ID_FORMULARIO, -1);
        }

        if ( (proyId != -1) && (formId != -1) ){
            mostrarListaElementos();
        }

    }

    private void mostrarListaElementos() {
        ArrayList<Elemento> listaElementos = repo.getElementos(proyId);
        ArrayList<String> nombreElementos = new ArrayList<String>();
        for (Elemento e: listaElementos) {
            nombreElementos.add(e.getNombre());
        }
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, nombreElementos);
        atv_nombreElem.setAdapter(adapter);
    }


    public void agregarElementos(View view){
        String elem = atv_nombreElem.getText().toString();
        atv_nombreElem.setText("");
        elem_seleccionados.add(elem);
        elementosSeleccionados.setAdapter(adapterSeleccion);
    }

    public void agregarAudio(View view){
        Intent i = new Intent(this, GrabadoraAudio.class);
        startActivityForResult(i, ACTIVITY_GRABAR_AUDIO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_GRABAR_AUDIO) {
            if (resultCode == Activity.RESULT_OK) {
                pathAudio = data.getStringExtra(AUDIO);
            }
        }
    }

    public void agregarFoto(View view) {
        Intent i = new Intent(this, GrabadoraAudio.class);
        startActivityForResult(i, ACTIVITY_FOTO);
    }

    public void verPlano(View view) {
        Intent i = new Intent(this, AyudaPlanilla.class);
        startActivityForResult(i, ACTIVITY_PLANO);
    }


    public void guardarFormulario(View view){
        boolean exito;
        if (!actualizarElementos()) {
            Toast.makeText(this, "Ningun elemento seleccionado", Toast.LENGTH_SHORT).show();
        }else {
            /////solo guarda AUDIO para probar
            exito = repo.agregarDatosFormulario(formId, pathAudio);

            if (exito) {
                Intent intent = new Intent(this, Principal.class);
                intent.putExtra(Principal.NOMBRE_PROYECTO, getIntent().getStringExtra(NOMBRE_PROYECTO));
                intent.putExtra(Principal.DIAGRAMA, getIntent().getStringExtra(DIAGRAMA));
                startActivity(intent);
                finish();
            }
        }
    }

    private boolean actualizarElementos() {
        boolean exito= false;
        for (String s: elem_seleccionados) {
            /////// SI NO EXISTE EN DB CREARLO
            int elemId = repo.getIdElemento(s, proyId);
            exito = repo.actualizarElemento(elemId, formId);
            Log.e("EXITO ACTULIZAR ELEM", ""+exito);
        }
        return exito;
    }

}