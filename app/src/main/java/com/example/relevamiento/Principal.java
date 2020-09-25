package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.modelos.Formulario;
import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;
import com.example.relevamiento.repositorio.parsers.MyAdapter;
import com.example.relevamiento.repositorio.parsers.StatusAdapter;
import com.example.relevamiento.repositorio.parsers.parser_marcas;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            proyectoSeleccionado = repo.getProyecto(nombreProyecto);
        }

        if (getIntent().hasExtra(DIAGRAMA)) {
            diagramaActual = getIntent().getStringExtra(DIAGRAMA);
        }else{
            diagramaActual = proyectoSeleccionado.getDiagramas().get(0);
        }

        setTitle("Proyecto: "+nombreProyecto);

        mostrarDatosProyectoPantalla();
    }


    private void mostrarDatosProyectoPantalla() {
        mostrarDiagrama();
        mostrarElementos();
    }

    private void mostrarElementos() {
        ArrayList<Elemento> listaElementos = getElementos(proyectoSeleccionado.getId());
        int correctitud; //si no tiene formulario: correctitud es -1
        int formId;
        ArrayList<StatusAdapter> listaStatus = new ArrayList<>();

        for (Elemento e: listaElementos) {
            correctitud = -1;
            formId = e.getFormId();
            if (formId != 0) { //tiene formulario asociado
                correctitud = getCorrectitudFormulario(formId); // 1 o 0
            }
            listaStatus.add(new StatusAdapter (e.getNombre(), correctitud ));
            Log.e("STATUS", "nombre: "+e.getNombre()+" correctitud: "+correctitud);
        }

        MyAdapter adapter = new MyAdapter(this, listaStatus);
        lv_elementos.setAdapter(adapter);
    }





    private ArrayList<Elemento> getElementos(int proyId) { return repo.getElementos(proyId); }

    private int getCorrectitudFormulario(int formId) { return repo.getCorrectitudFormulario(formId); }

    private void mostrarDiagrama() {
        File imgFile = new File(diagramaActual);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv_diagrama.setImageBitmap(myBitmap); //setea el diagrama seleccionado

           mostrarFormularios(diagramaActual);
        }
    }


    private void mostrarFormularios(String pathDiagrama){
        ArrayList<Formulario> formularios = repo.getFormularios(pathDiagrama, proyectoSeleccionado.getId());
        for (Formulario form: formularios) {
            marcarEnDiagrama(form.getMarcas(), form.isEsCorrecto());
        }
    }


    private void marcarEnDiagrama(ArrayList<Integer> marcas, boolean correcto){
        int color;
        if (correcto)
            color = Color.GREEN;
        else
            color = Color.RED;

        BitmapDrawable drawable = (BitmapDrawable) iv_diagrama.getDrawable();
        Bitmap aux = drawable.getBitmap();
        Bitmap mutableBitMap = aux.copy(Bitmap.Config.ARGB_8888, true);

        Matrix invertMatrix = new Matrix();
        iv_diagrama.getImageMatrix().invert(invertMatrix);

        Canvas tempCanvas = new Canvas(mutableBitMap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAlpha(50);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        //paint.setStrokeWidth(3);
        tempCanvas.drawRect(marcas.get(0), marcas.get(1), marcas.get(2), marcas.get(3), paint);

        iv_diagrama.setImageBitmap(mutableBitMap);

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

    public void marcarFormulario(View view){
        Intent i = new Intent(this, Marcar.class);
        i.putExtra(Marcar.NOMBRE_PROYECTO, nombreProyecto);
        i.putExtra(Marcar.DIAGRAMA, diagramaActual);
        startActivity(i);
        finish();


    }



}