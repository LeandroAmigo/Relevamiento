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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Marcar extends AppCompatActivity {

    public static final String DIAGRAMA = "diagrama";
    public static final String MARCAS = "marcas";
    public static final String SWITCH = "switch";

    private String diagramaActual;
    private ArrayList<Integer> listaMarcas;
    private Switch switch_correcto;

    private TouchImageView iv_diagrama;
    private ImageView iv_zoomIn;
    private ImageView iv_zoomOut;
    private ImageView iv_left;
    private ImageView iv_right;
    private ImageView iv_up;
    private ImageView iv_down;
    private Bitmap myBitmap;

    private Button btn_aceptar;
    private boolean correcto = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar);

        listaMarcas = new ArrayList<>();

        if (getIntent().hasExtra(DIAGRAMA)) {
            diagramaActual = getIntent().getStringExtra(DIAGRAMA);
        }
        if (getIntent().hasExtra(MARCAS)){
            listaMarcas = getIntent().getIntegerArrayListExtra(MARCAS);
            correcto = getIntent().getBooleanExtra(SWITCH, true);
        }

        btn_aceptar =  findViewById(R.id.btn_aceptar_marcar);
        switch_correcto =  findViewById(R.id.switch_marcar);
        iv_diagrama = findViewById(R.id.iv_diagrama);
        iv_zoomIn =  findViewById(R.id.zoomIn);
        iv_zoomOut =  findViewById(R.id.zoomOut);
        iv_left =  findViewById(R.id.left);
        iv_right =  findViewById(R.id.right);
        iv_up =  findViewById(R.id.up);
        iv_down =  findViewById(R.id.down);
        mostrarDiagrama();

        if (correcto)
            switch_correcto.setChecked(true);
        else
            switch_correcto.setChecked(false);

        if (listaMarcas.size()==4){
            btn_aceptar.setEnabled(true);
        }

        if (!listaMarcas.isEmpty())
            marcarEnDiagrama(listaMarcas, switch_correcto.isChecked());

        //asigna los oyentes a las imagenes invisibles
        AsignarOyentesImageViewTouchable();
        //setea el oyente del click
        iv_diagrama.setOnTouchListener(handleTouch);

        //activar MOUSE
        Intent intent = new Intent("com.realwear.wearhf.intent.action.MOUSE_COMMANDS");
        intent.putExtra("com.realwear.wearhf.intent.extra.MOUSE_ENABLED", true);
        sendBroadcast(intent);

    }


    private void mostrarDiagrama() {
        File imgFile = new File(diagramaActual);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            cargarDiagramaEnBlanco(); // carga la imagen
        }
    }

    //cada vez que se hace click entra aca
    private View.OnTouchListener handleTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            boolean salida = true;
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (listaMarcas.size()<4) {
                    marcarEnDiagramaNuevoFormulario(motionEvent.getX(), motionEvent.getY());
                }
                else
                    salida =false;
            }
            return salida;
        }
    };


    public void switch_correcto (View view){
        if (listaMarcas.size() == 4){
            cargarDiagramaEnBlanco(); // carga la imagen
            if (switch_correcto.isChecked())
                marcarEnDiagrama(listaMarcas, true);
            else
                marcarEnDiagrama(listaMarcas, false);
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

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas tempCanvas = new Canvas(mutableBitMap);

        if (marcas.size() == 4) {
            paint.setColor(color);
            paint.setAlpha(50);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(3);
            ArrayList<Integer> marcasOrdenadas = ordenarMarcas(marcas); // x1, y1, x2, y2   siendo x1 < x2 && y1 < y2
            tempCanvas.drawRect(marcasOrdenadas.get(0), marcasOrdenadas.get(1), marcasOrdenadas.get(2), marcasOrdenadas.get(3), paint);

        }else if (marcas.size() == 2) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            tempCanvas.drawCircle(marcas.get(0), marcas.get(1), 12f, paint);
        }
        iv_diagrama.setImageBitmap(mutableBitMap);
    }

    private void AsignarOyentesImageViewTouchable() {
        iv_zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_diagrama.setMaxZoom(12);
                float zm = iv_diagrama.getCurrentZoom()* 5.5f;
                Toast.makeText(getApplicationContext(),"CURRENT ZOOM:  "+zm, Toast.LENGTH_SHORT).show();
                iv_diagrama.setZoom(zm, iv_diagrama.getScrollPosition().x, iv_diagrama.getScrollPosition().y);
            }
        });

        iv_zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float zm = iv_diagrama.getCurrentZoom()/ 5.5f;
                iv_diagrama.setZoom(zm, iv_diagrama.getScrollPosition().x, iv_diagrama.getScrollPosition().y);
                //iv_diagrama.resetZoom();
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

    private void marcarEnDiagramaNuevoFormulario(float x, float y) {
        //el bitmap actual se guarda en aux. se lo copia en mutableBitMap para modificarlo. se lo almacena en iv_imagen.. ciclo...
        BitmapDrawable drawable = (BitmapDrawable) iv_diagrama.getDrawable();
        Bitmap mutableBitMap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);

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

        listaMarcas.add(xCoordinate);
        listaMarcas.add(yCoordinate);
        if (listaMarcas.size() == 4){
            cargarDiagramaEnBlanco(); // carga la imagen
            btn_aceptar.setEnabled(true);
            marcarEnDiagrama(listaMarcas, switch_correcto.isChecked());

        }else if (listaMarcas.size() == 2){
            Paint paint = new Paint();
            Canvas tempCanvas = new Canvas(mutableBitMap);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            tempCanvas.drawCircle((float) xCoordinate,(float) yCoordinate, 12F, paint);
            iv_diagrama.setImageBitmap(mutableBitMap);
        }
    }


    private ArrayList<Integer> ordenarMarcas (ArrayList<Integer> listaDeMarcas){
        ArrayList<Integer> salida = new ArrayList<>(listaDeMarcas);
        Collections.copy(salida,listaDeMarcas);
        if (listaDeMarcas.get(0) > listaDeMarcas.get(2)){
            salida.set(0, listaDeMarcas.get(2));
            salida.set(2,listaDeMarcas.get(0));
        }
        if (listaDeMarcas.get(1) > listaDeMarcas.get(3)){ ;
            salida.set(1, listaDeMarcas.get(3));
            salida.set(3, listaDeMarcas.get(1));
        }
        return salida;
    }

    public void acpeptar(View view){
        Intent intent = new Intent("com.realwear.wearhf.intent.action.MOUSE_COMMANDS");
        intent.putExtra("com.realwear.wearhf.intent.extra.MOUSE_ENABLED", false);
        sendBroadcast(intent);

        Intent i = new Intent();
        i.putIntegerArrayListExtra(MARCAS, (ArrayList<Integer>) listaMarcas);
        i.putExtra(SWITCH, switch_correcto.isChecked());
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    public void cancelar (View view){
        if (listaMarcas.size() >= 2) {
            listaMarcas.remove(listaMarcas.size() - 1); // y2
            listaMarcas.remove(listaMarcas.size() - 1); //x2
            cargarDiagramaEnBlanco(); // carga la imagen
            if (listaMarcas.size() == 2) { // dibuja las marcas restantes
                marcarEnDiagrama(listaMarcas, switch_correcto.isChecked());
            }
        }
    }

    private void cargarDiagramaEnBlanco(){
        iv_diagrama.setImageBitmap(myBitmap);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }


}