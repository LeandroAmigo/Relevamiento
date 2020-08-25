package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btn_abrir, btn_nuevo;
    private DialogProperties properties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_abrir = (Button) findViewById(R.id.btn_abrir);
        btn_nuevo = (Button) findViewById(R.id.btn_nuevo);

        properties = new DialogProperties();
    }

    public void abrirProyecto (View view) {
        //traer todos los nombres de proyectos de la BD
        //mostrarlos en un Spinner
    }

    private void nuevoProyecto(View view){
        Intent i = new Intent(this, NuevoProyecto.class);
        startActivity(i);
    }


}