package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;
import com.ortiz.touchview.TouchImageView;

import java.io.File;

public class DiagramaCompleto extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";
    public static final String DIAGRAMA = "diagrama";

    private static final int ACTIVITY_CAMBIAR_DIAGRAMA = 1;

    private String nombreProyecto;
    private String diagramaActual;

    private TouchImageView iv_diagrama;
    private ImageView iv_zoomIn;
    private ImageView iv_zoomOut;
    private ImageView iv_left;
    private ImageView iv_right;
    private ImageView iv_up;
    private ImageView iv_down;

    private Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagrama_completo);

        iv_diagrama = (TouchImageView) findViewById(R.id.iv_diagrama);
        iv_zoomIn = (ImageView) findViewById(R.id.zoomIn);
        iv_zoomOut = (ImageView) findViewById(R.id.zoomOut);
        iv_left = (ImageView) findViewById(R.id.left);
        iv_right = (ImageView) findViewById(R.id.right);
        iv_up = (ImageView) findViewById(R.id.up);
        iv_down = (ImageView) findViewById(R.id.down);

        //asigna los oyentes a las imagenes invisibles
        AsignarOyentesImageViewTouchable();

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
            iv_diagrama.setImageBitmap(myBitmap); // carga la imagen
            iv_diagrama.resetZoom();
        }
    }


    private void AsignarOyentesImageViewTouchable() {
        iv_zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_diagrama.setMaxZoom(10);
                float zm = iv_diagrama.getCurrentZoom()* 2.0f;
                Toast.makeText(getApplicationContext(),"CURRENT ZOOM:  "+zm, Toast.LENGTH_SHORT).show();
                iv_diagrama.setZoom(zm, iv_diagrama.getScrollPosition().x, iv_diagrama.getScrollPosition().y);
            }
        });

        iv_zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float zm = iv_diagrama.getCurrentZoom()/ 2.0f;
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