package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.relevamiento.repositorio.Repositorio;

import java.util.ArrayList;
import java.util.Collections;

public class ListadoProyectos extends AppCompatActivity {

    private Spinner spinner;
    private String seleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_proyectos);

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<String> nombreProyectos = getProyectos();
        Collections.reverse(nombreProyectos);
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
            Intent intent = new Intent(this, Principal.class);
            intent.putExtra(Principal.NOMBRE_PROYECTO, seleccion);
            startActivity(intent);
            finish();
        }
    }

    public void editarProyecto(View view){
        if (spinner.getSelectedItem() != null ) {
            seleccion = spinner.getSelectedItem().toString();
            Intent intent = new Intent(this, CrearCargarProyecto.class);
            intent.putExtra(CrearCargarProyecto.NOMBRE_PROYECTO, seleccion);
            startActivity(intent);
            finish();
        }
    }

    public void exportarProyecto(View view){
        if (spinner.getSelectedItem() != null ) {
            seleccion = spinner.getSelectedItem().toString();
            Intent intent = new Intent(this, Exportar2.class);
            intent.putExtra(Exportar2.NOMBRE_PROYECTO, seleccion);
            startActivityForResult(intent, 2);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            Toast.makeText(this, "Datos exportados exitosamente", Toast.LENGTH_SHORT).show();
            //do the things u wanted
        }
    }


}