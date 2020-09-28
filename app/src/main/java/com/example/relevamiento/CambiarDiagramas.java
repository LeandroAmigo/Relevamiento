package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;
import com.example.relevamiento.repositorio.parsers.CustomAdapter;

import java.util.ArrayList;

public class CambiarDiagramas extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";

    private String pathDiagrama = null, nombreProyecto = null;
    private Proyecto proyectoSeleccionado;
    private Repositorio repo;

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_diagramas);

        gridView = findViewById(R.id.gridview);

        repo = new Repositorio(this);

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
            proyectoSeleccionado = getProyecto(nombreProyecto);
        }

        mostrarDiagramas();

    }

    private void mostrarDiagramas() {
        final ArrayList<String> diagramas = proyectoSeleccionado.getDiagramas();
        CustomAdapter customAdapter = new CustomAdapter(this, diagramas);
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // resaltar seleccionado y obtener el string en ""pathDiagrama""
                pathDiagrama = diagramas.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(DiagramaCompleto.DIAGRAMA, pathDiagrama);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    private Proyecto getProyecto(String nombreProyecto){
        return repo.getProyecto(nombreProyecto);
    }






}