package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class ListadoProyectos extends AppCompatActivity {

    private Spinner spinner;
    private String seleccion;
    private Intent intent;
    private Bundle bundle;

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
        ArrayList<String> nombreProyectos = new ArrayList<String>();
        SQLiteDatabase BaseDeDatos = MainActivity.getBD().getWritableDatabase();
        Cursor c = BaseDeDatos.rawQuery("select proyecto_nombre from proyectos", null);
        if (c.moveToFirst()){
            do {
                nombreProyectos.add(c.getString(0)); //"select 0,1,2,3... from proyectos"
            } while(c.moveToNext());
        }
        BaseDeDatos.close();
        return nombreProyectos;
    }



    public void abrirProyecto(View view){
        seleccion = spinner.getSelectedItem().toString();
        intent = new Intent(this, Principal.class );
        intent.putExtra(Principal.NOMBRE_PROYECTO, seleccion);
        startActivity(intent);

    }

    public void editarProyecto(View view){
        seleccion = spinner.getSelectedItem().toString();

    }

   /* public void exportarProyecto(View view){
        /// a futuro
    }

    */





}