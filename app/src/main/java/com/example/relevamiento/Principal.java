package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.modelos.Formulario;
import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;
import com.example.relevamiento.repositorio.parsers.MyAdapter;
import com.example.relevamiento.repositorio.parsers.StatusAdapter;

import java.io.File;
import java.util.ArrayList;


public class Principal extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";
    public static final String DIAGRAMA = "diagrama";

    private String nombreProyecto;
    private String diagramaActual;
    private Proyecto proyectoSeleccionado;
    private Repositorio repo;
    private ImageView iv_diagrama;
    private Bitmap myBitmap;
    private ListView lv_elementos;
    private TextView tv_avance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        iv_diagrama =  findViewById(R.id.diagrama);
        lv_elementos =  findViewById(R.id.listaElem);
        tv_avance =  findViewById(R.id.tv_avance);

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
        int cant = 0;
        ArrayList<Elemento> listaElementos = getElementos(proyectoSeleccionado.getId());
        int correctitud; //si no tiene formulario: correctitud es -1
        int formId;
        ArrayList<StatusAdapter> listaStatus = new ArrayList<>();

        for (Elemento e: listaElementos) {
            correctitud = -1;
            formId = e.getFormId();
            if (formId != 0) { //tiene formulario asociado
                correctitud = getCorrectitudFormulario(formId); // 1 o 0
                cant++; // para porcentaje
            }
            listaStatus.add(new StatusAdapter (e.getNombre(), correctitud ));
            Log.e("STATUS", "nombre: "+e.getNombre()+" correctitud: "+correctitud);
        }

        MyAdapter adapter = new MyAdapter(this, listaStatus);
        lv_elementos.setAdapter(adapter);
        //actualizar avance
        if (listaElementos.size()>0) {
            tv_avance.setText("Completado: " + ((cant * 100) / listaElementos.size()) + "% ");
            Log.e("sda", "Completado: " + ((cant * 100) / listaElementos.size()) + "% ");
        }
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
        tempCanvas.drawRect(marcas.get(0), marcas.get(1), marcas.get(2), marcas.get(3), paint);

        iv_diagrama.setImageBitmap(mutableBitMap);

    }

    public void cambiarDiagramas(View view){
        //   https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
        Intent i = new Intent(this, DiagramaCompleto.class);
        i.putExtra(DiagramaCompleto.NOMBRE_PROYECTO, nombreProyecto);
        i.putExtra(DiagramaCompleto.DIAGRAMA, diagramaActual);
        startActivity(i);
        //finish();
    }

    public void nuevoFormulario(View view){
        Intent i = new Intent(this, Planilla.class);
        i.putExtra(Marcar.NOMBRE_PROYECTO, nombreProyecto);
        i.putExtra(Marcar.DIAGRAMA, diagramaActual);
        startActivity(i);
        //finish();
    }

    public void editarFormulario(View view){
        Intent i = new Intent(this, PlanillaEditar.class);
        i.putExtra(Marcar.NOMBRE_PROYECTO, nombreProyecto);
        i.putExtra(Marcar.DIAGRAMA, diagramaActual);
        startActivity(i);
    }

    @Override
    public void onBackPressed (){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();

    }



}