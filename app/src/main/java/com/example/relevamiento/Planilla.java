package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.modelos.Formulario;
import com.example.relevamiento.repositorio.Repositorio;
import com.example.relevamiento.repositorio.parsers.MyAdapter;
import com.example.relevamiento.repositorio.parsers.StatusAdapter;

import java.util.ArrayList;

public class Planilla extends AppCompatActivity {

    public static final String ID_PROYECTO = "proyecto_id";
    public static final String ID_FORMULARIO = "formulario_id";

    public static final String AUDIO = "audio";
    public static final String FOTO = "foto";

    private static final int ACTIVITY_GRABAR_AUDIO = 1;
    private static final int ACTIVITY_FOTO = 2;
    private static final int ACTIVITY_PLANO = 3;


    private Repositorio repo;
    private Formulario formulario;
    private int proyId, formId;
    private Spinner spinner;
    private Button btn_agregar;

    private ArrayList<String> elem_seleccionados;
    private ArrayAdapter<String> adapter;
    private String pathAudio, pathFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planilla);

        spinner = (Spinner) findViewById(R.id.spinner_elem);

        repo = new Repositorio(this);

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
        spinner.setAdapter(adapter);
        //inicializa lista
        elem_seleccionados = new ArrayList<String>();
    }


    public void agregarElementos(View view){
        String elem = spinner.getSelectedItem().toString();
        elem_seleccionados.add(elem);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, elem_seleccionados);
        /////quitarlo de spinner
    }

    public void agregarAudio(View view){
        Intent i = new Intent(this, GrabadoraAudio.class);
        startActivityForResult(i, ACTIVITY_GRABAR_AUDIO);
    }

    public void agregarFoto(View view) {
        Intent i = new Intent(this, GrabadoraAudio.class);
        startActivityForResult(i, ACTIVITY_FOTO);
    }

    public void verPlano(View view) {
        Intent i = new Intent(this, AyudaPlanilla.class);
        startActivityForResult(i, ACTIVITY_PLANO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_GRABAR_AUDIO) {
            if (resultCode == Activity.RESULT_OK) {
                pathAudio = data.getStringExtra(AUDIO);
            }
        }
        if (requestCode == ACTIVITY_FOTO) {
            if (resultCode == Activity.RESULT_OK) {
                pathFoto = data.getStringExtra(FOTO);
            }
        }
        if (requestCode == ACTIVITY_PLANO) {

        }

    }

    public void guardarFormulario(View view){
        //agregar formId a cada elemento;
        //actualizar el formulario con foto, audio, imagen
        //finish(); volver a principal
    }

}