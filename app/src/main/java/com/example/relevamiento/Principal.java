package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.modelos.Formulario;
import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;
import com.example.relevamiento.repositorio.parsers.parser_marcas;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";
    public static final String DIAGRAMA = "diagrama";
    private static final int ACTIVITY_CAMBIAR_DIAGRAMA = 1;

    private String nombreProyecto;
    private String diagramaActual;
    private Proyecto proyectoSeleccionado;
    private Repositorio repo;
    private ImageView iv_diagrama;
    private Bitmap myBitmap;
    private ListView lv_elementos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        iv_diagrama = (ImageView) findViewById(R.id.diagrama);
        lv_elementos = (ListView) findViewById(R.id.listaElem);

        repo = new Repositorio(this);

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
            proyectoSeleccionado = getProyecto(nombreProyecto);
        }
        mostrarDatosProyectoPantalla();
    }

    private void mostrarDatosProyectoPantalla() {
        diagramaActual = proyectoSeleccionado.getDiagramas().get(0);
        mostrarDiagrama();
        mostrarElementos();
       // mostrarFormularios(diagramaActual);
    }

    private void mostrarElementos() {
        ArrayList<Elemento> listaElementos = getElementos(proyectoSeleccionado.getId());
        ArrayList<String> listaNombreElementos = new ArrayList<String>();
        for (Elemento e: listaElementos) {
            listaNombreElementos.add(e.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaNombreElementos);
        lv_elementos.setAdapter(adapter);
        //// ASIGNAR OYENTE A CADA ELEMENTO
    }

    private void mostrarDiagrama() {
        File imgFile = new File(diagramaActual);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv_diagrama.setImageBitmap(myBitmap); //setea el diagrama seleccionado
        }
    }

    private ArrayList<Elemento> getElementos(int proyId) {
        return repo.getElementos(proyId);
    }

    private Proyecto getProyecto(String nombreProyecto){
        return repo.getProyecto(nombreProyecto);
    }


    private void mostrarFormularios(String pathDiagrama){
        ArrayList<Formulario> formularios = repo.getFormularios(pathDiagrama, proyectoSeleccionado.getId());
        for (Formulario form: formularios) {
            marcarEnDiagrama(form.getMarcas(), form.isEsCorrecto());
            //colorearElementos(form.getId());

        }
    }

    private void colorearElementos(int id) {

    }


    public void cambiarDiagramas(){
     //   https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
        Intent i = new Intent(this, CambiarDiagramas.class);
        startActivityForResult(i, ACTIVITY_CAMBIAR_DIAGRAMA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_CAMBIAR_DIAGRAMA) {
            if (resultCode == Activity.RESULT_OK) {
                String diagramaActual = data.getStringExtra(DIAGRAMA);
                mostrarDiagrama();
            }
        }
    }


    public void marcarEnDiagrama(ArrayList<Float> marcas, boolean correcto){
        int color;
        if (correcto)
            color = Color.GREEN;
        else
            color = Color.RED;

        BitmapDrawable drawable = (BitmapDrawable) iv_diagrama.getDrawable();
        Bitmap aux = drawable.getBitmap();
        Bitmap mutableBitMap = aux.copy(Bitmap.Config.ARGB_8888, true);

        Canvas tempCanvas = new Canvas(mutableBitMap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAlpha(50);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        //paint.setStrokeWidth(3);
        tempCanvas.drawRect((float)marcas.get(1), (float)marcas.get(0), (float)marcas.get(3), (float)marcas.get(2), paint);

        iv_diagrama.setImageBitmap(mutableBitMap);

    }



}