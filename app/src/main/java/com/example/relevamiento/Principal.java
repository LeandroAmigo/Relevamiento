package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.relevamiento.repositorio.Repositorio;

public class Principal extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            String nombreProyectoSeleccionadro = getIntent().getStringExtra(NOMBRE_PROYECTO);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + NOMBRE_PROYECTO);
        }

       // Repositorio r = new Repositorio(this);
       // r.metodo()

        //cargarDiagramas();
        //cargarElementos();


    }
}