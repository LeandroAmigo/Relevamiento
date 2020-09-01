package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class Principal extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        String nombreProyectoSeleccionadro = null;
        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyectoSeleccionadro = getIntent().getStringExtra(NOMBRE_PROYECTO);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + NOMBRE_PROYECTO);
        }

        SQLiteDatabase BaseDeDatos = MainActivity.getBD().getWritableDatabase();

        //cargarDiagramas();
        //cargarElementos();

    }
}