package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.relevamiento.modelos.Formulario;
import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;
import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.util.ArrayList;

public class Marcar extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";
    public static final String DIAGRAMA = "diagrama";

    private String nombreProyecto;
    private String diagramaActual;
    private Proyecto proyectoSeleccionado;
    private Repositorio repo;
    private Bitmap myBitmap;
    private int color;
    private ArrayList<Float> listaMarcas;

    private TouchImageView iv_diagrama;
    private ImageView iv_zoomIn;
    private ImageView iv_zoomOut;
    private ImageView iv_left;
    private ImageView iv_right;
    private ImageView iv_up;
    private ImageView iv_down;
    private Switch switch_correcto;
    private Button btn_aceptar;
    private Button btn_cancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar);

        repo = new Repositorio(this);
        listaMarcas = new ArrayList<Float>();

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
            diagramaActual = getIntent().getStringExtra(DIAGRAMA);
            proyectoSeleccionado = repo.getProyecto(nombreProyecto);
        }

        btn_aceptar = (Button) findViewById(R.id.btn_aceptar_marcar);
        btn_cancelar = (Button) findViewById(R.id.btn_cancelar_marcar);
        switch_correcto = (Switch) findViewById(R.id.switch_marcar);
        iv_diagrama = (TouchImageView) findViewById(R.id.iv_diagrama);
        iv_zoomIn = (ImageView) findViewById(R.id.zoomIn);
        iv_zoomOut = (ImageView) findViewById(R.id.zoomOut);
        iv_left = (ImageView) findViewById(R.id.left);
        iv_right = (ImageView) findViewById(R.id.right);
        iv_up = (ImageView) findViewById(R.id.up);
        iv_down = (ImageView) findViewById(R.id.down);

        mostrarDiagrama();

        //asigna los oyentes a las imagenes invisibles
        AsignarOyentesImageViewTouchable();

        //activar MOUSE
        Intent intent = new Intent("com.realwear.wearhf.intent.action.MOUSE_COMMANDS");
        intent.putExtra("com.realwear.wearhf.intent.extra.MOUSE_ENABLED", true);

    }


    private void mostrarDiagrama() {
        File imgFile = new File(diagramaActual);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv_diagrama.setImageBitmap(myBitmap); //setea el diagrama seleccionado

            mostrarFormularios(diagramaActual);
        }
    }

    //cada vez que se hace click entra aca
    private View.OnTouchListener handleTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (switch_correcto.isChecked())
                color = Color.GREEN;
            else
                color = Color.RED;

            listaMarcas.add(motionEvent.getX());
            listaMarcas.add(motionEvent.getY());
            marcarEnDiagramaNuevoFormulario(motionEvent.getX(), motionEvent.getY(), color);

            if (listaMarcas.size() == 4) {
                ordenarMarcas(); // x1, y1, x2, y2   siendo x1 < x2 && y1 < y2
                btn_aceptar.setEnabled(true);
            }

            return true;
        }
    };



    private void mostrarFormularios(String pathDiagrama){
        ArrayList<Formulario> formularios = repo.getFormularios(pathDiagrama, proyectoSeleccionado.getId());
        for (Formulario form: formularios) {
            marcarEnDiagrama(form.getMarcas(), form.isEsCorrecto());
        }
    }


    private void marcarEnDiagrama(ArrayList<Float> marcas, boolean correcto){
        int color;
        if (correcto)
            color = Color.GREEN;
        else
            color = Color.RED;

        BitmapDrawable drawable = (BitmapDrawable) iv_diagrama.getDrawable();
        Bitmap aux = drawable.getBitmap();
        Bitmap mutableBitMap = aux.copy(Bitmap.Config.ARGB_8888, true);


        float [] eventXY = {marcas.get(0) , marcas.get(1) , marcas.get(2) , marcas.get(3) };

        Matrix invertMatrix = new Matrix();
        iv_diagrama.getImageMatrix().invert(invertMatrix);
        invertMatrix.mapPoints(eventXY);

        int x1Coordinate = Math.round(eventXY[0]);
        int y1Coordinate = Math.round(eventXY[1]);
        int x2Coordinate = Math.round(eventXY[2]);
        int y2Coordinate = Math.round(eventXY[3]);

        if (x1Coordinate < 0) {
            x1Coordinate = 0;
        } else if (x1Coordinate > mutableBitMap.getWidth() - 1) {
            x1Coordinate = mutableBitMap.getWidth() - 1;
        }

        if (y1Coordinate < 0) {
            y1Coordinate = 0;
        } else if (y1Coordinate > mutableBitMap.getHeight() - 1) {
            y1Coordinate = mutableBitMap.getHeight() - 1;
        }
        if (x2Coordinate < 0) {
            x2Coordinate = 0;
        } else if (x2Coordinate > mutableBitMap.getWidth() - 1) {
            x2Coordinate = mutableBitMap.getWidth() - 1;
        }

        if (y2Coordinate < 0) {
            y2Coordinate = 0;
        } else if (y2Coordinate > mutableBitMap.getHeight() - 1) {
            y2Coordinate = mutableBitMap.getHeight() - 1;
        }

        Canvas tempCanvas = new Canvas(mutableBitMap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAlpha(96); //transparencia
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        //paint.setStrokeWidth(3);
        tempCanvas.drawRect((float)x1Coordinate, (float)y1Coordinate, (float)x2Coordinate, (float)y2Coordinate, paint);

        iv_diagrama.setImageBitmap(mutableBitMap);

    }


    private void AsignarOyentesImageViewTouchable() {
        iv_diagrama.setOnTouchListener(handleTouch); //setea el oyente del click

        iv_zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //editor = sharedpreferences.edit();
                iv_diagrama.setMaxZoom(12);
                float zm = iv_diagrama.getCurrentZoom()* 5.5f;
                Toast.makeText(getApplicationContext(),"CURRENT ZOOM:  "+zm, Toast.LENGTH_SHORT).show();
                iv_diagrama.setZoom(zm, iv_diagrama.getScrollPosition().x, iv_diagrama.getScrollPosition().y);
            }
        });

        iv_zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_diagrama.resetZoom();
            }
        });

        iv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_diagrama.setScrollPosition(iv_diagrama.getScrollPosition().x, iv_diagrama.getScrollPosition().y - 0.1F);
            }
        });

        iv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_diagrama.setScrollPosition(iv_diagrama.getScrollPosition().x, iv_diagrama.getScrollPosition().y + 0.1F);
            }
        });

        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_diagrama.setScrollPosition(iv_diagrama.getScrollPosition().x - 0.1F, iv_diagrama.getScrollPosition().y);
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_diagrama.setScrollPosition(iv_diagrama.getScrollPosition().x + 0.1F, iv_diagrama.getScrollPosition().y);
            }
        });

    }


    private void marcarEnDiagramaNuevoFormulario(float x, float y, int color) {
        //el bitmap actual se guarda en aux. se lo copia en mutableBitMap para modificarlo. se lo almacena en iv_imagen.. ciclo...
        BitmapDrawable drawable = (BitmapDrawable) iv_diagrama.getDrawable();
        Bitmap aux = drawable.getBitmap();

        Bitmap mutableBitMap = aux.copy(Bitmap.Config.ARGB_8888, true);

        float [] eventXY = {x , y};

        Matrix invertMatrix = new Matrix();
        iv_diagrama.getImageMatrix().invert(invertMatrix);
        invertMatrix.mapPoints(eventXY);

        int xCoordinate = Math.round(eventXY[0]);
        int yCoordinate = Math.round(eventXY[1]);

        if (xCoordinate < 0) {
            xCoordinate = 0;
        } else if (xCoordinate > mutableBitMap.getWidth() - 1) {
            xCoordinate = mutableBitMap.getWidth() - 1;
        }

        if (yCoordinate < 0) {
            yCoordinate = 0;
        } else if (yCoordinate > mutableBitMap.getHeight() - 1) {
            yCoordinate = mutableBitMap.getHeight() - 1;
        }

        //Create a new image bitmap and attach a brand new canvas to it
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setAntiAlias(true);

        Canvas tempCanvas = new Canvas(mutableBitMap);
        tempCanvas.drawCircle(  (float) xCoordinate, // cx
                                (float) yCoordinate, // cy
                                15F, // Radius
                                paint // Paint
        );
        iv_diagrama.setImageBitmap(mutableBitMap);
    }

    private void ordenarMarcas(){
        float aux;
        if (listaMarcas.get(0) > listaMarcas.get(2)){
            aux = listaMarcas.get(0);
            listaMarcas.set(0, listaMarcas.get(2));
            listaMarcas.set(2,aux);
        }
        if (listaMarcas.get(1) > listaMarcas.get(3)){
            aux = listaMarcas.get(1);
            listaMarcas.set(1, listaMarcas.get(3));
            listaMarcas.set(3,aux);
        }
    }


    public void acpeptar(View view){
        Toast.makeText(this, "MARCAS: "+listaMarcas.get(0)+" - "+listaMarcas.get(1)+" - "+listaMarcas.get(2)+" - "+listaMarcas.get(3), Toast.LENGTH_SHORT ).show();
        //intent planilla
        //guardar BD (formulario) diagramas, marcas, correctitud
        //deshabilitar mira
    }

    public void cancelar (View view){
        if (listaMarcas.size() >= 2) {
            listaMarcas.remove(listaMarcas.size() - 1); // y2
            listaMarcas.remove(listaMarcas.size() - 1); // x2
        }
    }


}