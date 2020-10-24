package com.example.relevamiento;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.Toast;

import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.modelos.Formulario;
import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
/*
public class Exportar {
    private Context context;
    private Repositorio repo;

    private String nombreProyecto;
    private int proyId;

    public Exportar (Context context){
        this.context = context;
        repo = new Repositorio(context);
        nombreProyecto = null;
        proyId = -1;
    }

    public void exportarProyecto(String nombreProyecto) {
        Proyecto proy = repo.getProyecto(nombreProyecto);
        this.proyId = proy.getId();
        this.nombreProyecto = nombreProyecto;

        crearCarpetaInicial();

        for (String diagrama: proy.getDiagramas()) {
            exportarFormularios(diagrama);
        }

        exportarElementos(proyId);
    }

    private void crearCarpetaInicial() {
        File f = new File(Environment.getExternalStorageDirectory(), nombreProyecto);
        if (!f.exists()) {
            f.mkdirs();
        }
    }


    private void exportarFormularios(String pathDiagrama){
        File imgFile = new File(pathDiagrama);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv_diagrama.setImageBitmap(myBitmap); //setea el diagrama seleccionado
        }
        ArrayList<Formulario> formularios = repo.getFormularios(pathDiagrama, proyId);
        for (Formulario form: formularios) {
            marcarEnDiagrama(form.getMarcas(), form.isEsCorrecto());
        }
        // crear bitmap con nombre formID y retornar path
        BitmapDrawable drawable = (BitmapDrawable) iv_diagrama.getDrawable();
        Bitmap aux = drawable.getBitmap();
        Bitmap mutableBitMap = aux.copy(Bitmap.Config.ARGB_8888, true);
        exportarDiagrama(mutableBitMap, pathDiagrama);

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

    private void exportarDiagrama(Bitmap finalBitmap, String pathDiagrama) {
        File f = new File(Environment.getExternalStorageDirectory(), nombreProyecto);
       // String fname = "Image-"+ currentDateandTime +".jpg";
        File file = new File (f, pathDiagrama);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportarElementos(int proyId) {
        File carpeta = new File(Environment.getExternalStorageDirectory(), nombreProyecto);
        String archivoAgenda = carpeta.toString() + "/" + "elementosExportados.csv";
        boolean isCreate = false;
        int formId, correctitud;

        ArrayList<Elemento> listaElementos = repo.getElementos(proyId);
        for (Elemento e : listaElementos) {
            correctitud = -1;
            formId = e.getFormId();
            if (formId != 0) { //tiene formulario asociado
                correctitud = getCorrectitudFormulario(formId); // 1 o 0
            }
            ///CSV
            try {
                FileWriter fileWriter = new FileWriter(archivoAgenda);
                fileWriter.append(""+e.getNombre());
                fileWriter.append(";");
                if (correctitud == 1) {
                    fileWriter.append("correcto");
                }else if (correctitud == 0){
                    fileWriter.append("incorrecto");
                } else {
                    fileWriter.append("no relevado");
                }
                fileWriter.append(";");
                fileWriter.append("\n");
                fileWriter.close();
            } catch (Exception exc) { }
        }
    }

    private int getCorrectitudFormulario(int formId) { return repo.getCorrectitudFormulario(formId); }



}

 */





