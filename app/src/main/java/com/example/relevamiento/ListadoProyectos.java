package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.relevamiento.db.DataBaseHelper;
import com.example.relevamiento.repositorio.Repositorio;

import java.util.ArrayList;

public class ListadoProyectos extends AppCompatActivity {

    private Spinner spinner;
    private String seleccion;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_proyectos);

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<String> nombreProyectos = getProyectos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_proyectos, nombreProyectos);
        spinner.setAdapter(adapter);
    }

    private ArrayList<String> getProyectos(){
        Repositorio repo = new Repositorio(this);
        return repo.getProyectos();
    }


    public void abrirProyecto(View view){
        if (spinner.getSelectedItem() != null ) {
            seleccion = spinner.getSelectedItem().toString();
            intent = new Intent(this, Principal.class);
            intent.putExtra(Principal.NOMBRE_PROYECTO, seleccion);
            startActivity(intent);
            finish();
        }
    }

    public void editarProyecto(View view){
        seleccion = spinner.getSelectedItem().toString();
        intent = new Intent(this, CrearCargarProyecto.class );
        intent.putExtra(CrearCargarProyecto.NOMBRE_PROYECTO, seleccion);
        startActivity(intent);
        finish();

    }

   /* public void exportarProyecto(View view){
        /// a futuro
    }

    */

}