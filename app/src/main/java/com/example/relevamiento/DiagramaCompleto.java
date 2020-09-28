package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;

import java.io.File;

public class DiagramaCompleto extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";
    public static final String DIAGRAMA = "diagrama";

    private static final int ACTIVITY_CAMBIAR_DIAGRAMA = 1;

    private String nombreProyecto;
    private String diagramaActual;

    private ImageView iv_diagrama;
    private Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagrama_completo);

        iv_diagrama = (ImageView) findViewById(R.id.diagrama);

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
        }
        if (getIntent().hasExtra(DIAGRAMA)) {
            diagramaActual = getIntent().getStringExtra(DIAGRAMA);
            mostrarDiagrama();
        }
    }

    private void mostrarDiagrama() {
        File imgFile = new File(diagramaActual);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv_diagrama.setImageBitmap(myBitmap); //setea el diagrama seleccionado
        }
    }


    public void cambiarDiagramas(View view){
        //   https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
        Intent i = new Intent(this, CambiarDiagramas.class);
        i.putExtra(CambiarDiagramas.NOMBRE_PROYECTO, nombreProyecto);
        startActivityForResult(i, ACTIVITY_CAMBIAR_DIAGRAMA);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_CAMBIAR_DIAGRAMA) {
            if (resultCode == Activity.RESULT_OK) {
                diagramaActual = data.getStringExtra(DIAGRAMA);
                mostrarDiagrama();
            }
        }
    }

    public void aceptarDiagrama( View view){
        Intent intent = new Intent(this, Principal.class);
        intent.putExtra(Principal.NOMBRE_PROYECTO, nombreProyecto);
        intent.putExtra(Principal.DIAGRAMA, diagramaActual);
        startActivity(intent);
        finish();
    }





}