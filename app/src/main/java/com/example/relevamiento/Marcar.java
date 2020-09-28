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
    private ArrayList<Integer> listaMarcas;


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
        listaMarcas = new ArrayList<>();

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
        sendBroadcast(intent);

    }


    private void mostrarDiagrama() {
        File imgFile = new File(diagramaActual);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv_diagrama.setImageBitmap(myBitmap); // carga la imagen
        }
    }

    //cada vez que se hace click entra aca
    private View.OnTouchListener handleTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            marcarEnDiagramaNuevoFormulario(motionEvent.getX(), motionEvent.getY());
        }
        return true;
        }
    };

  /*  private void mostrarFormularios(String pathDiagrama){
        ArrayList<Formulario> formularios = repo.getFormularios(pathDiagrama, proyectoSeleccionado.getId());
        for (Formulario form: formularios) {
            marcarEnDiagrama(form.getMarcas(), form.isEsCorrecto());
        }
    }*/



    public void switch_correcto (View view){
        if (listaMarcas.size() == 4){
            iv_diagrama.setImageBitmap(myBitmap); // carga la imagen
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
            tempCanvas.drawRect(marcas.get(0), marcas.get(1), marcas.get(2), marcas.get(3), paint);

        }else if (marcas.size() == 2) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            tempCanvas.drawCircle(marcas.get(0), marcas.get(1), 12f, paint);
        }
        iv_diagrama.setImageBitmap(mutableBitMap);
    }

    private void AsignarOyentesImageViewTouchable() {
        iv_diagrama.setOnTouchListener(handleTouch); //setea el oyente del click

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

        listaMarcas.add(xCoordinate);
        listaMarcas.add(yCoordinate);
        if (listaMarcas.size() == 4){
            ordenarMarcas(); // x1, y1, x2, y2   siendo x1 < x2 && y1 < y2
            iv_diagrama.setImageBitmap(myBitmap); // carga la imagen
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


    private void ordenarMarcas(){
        int aux;
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
        //guardar diagramas, marcas y correctitud del Formulario en BD
        int formId = repo.crearFormulario(proyectoSeleccionado.getId(), diagramaActual, listaMarcas, switch_correcto.isChecked());
        //deshabilitar MOUSE
        Intent intent = new Intent("com.realwear.wearhf.intent.action.MOUSE_COMMANDS");
        intent.putExtra("com.realwear.wearhf.intent.extra.MOUSE_DISABLED", true);
        sendBroadcast(intent);

        if (formId != -1){
            Intent i = new Intent(this, Planilla.class);
            i.putExtra(Planilla.ID_PROYECTO, proyectoSeleccionado.getId());
            i.putExtra(Planilla.ID_FORMULARIO, formId);
            i.putExtra(Planilla.NOMBRE_PROYECTO, nombreProyecto);
            i.putExtra(Planilla.DIAGRAMA, diagramaActual);
            startActivity(i);
            finish();
        }
    }

    public void cancelar (View view){
        if (listaMarcas.size() >= 2) {
            listaMarcas.remove(listaMarcas.size() - 1); // y2
            listaMarcas.remove(listaMarcas.size() - 1); //x2
            iv_diagrama.setImageBitmap(myBitmap); // carga la imagen
            if (listaMarcas.size() == 2) {
                marcarEnDiagrama(listaMarcas, switch_correcto.isChecked());
            }
        }
    }


}