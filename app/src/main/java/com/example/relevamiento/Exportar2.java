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
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.modelos.Formulario;
import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import static com.example.relevamiento.repositorio.parsers.parser_audiosYfotos.convertListToString;

public class Exportar2 extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";
    private Repositorio repo;
    private String nombreProyecto;
    private int proyId;
    private ImageView iv_diagrama;
    private Button button1;
    private TextView tv_infoExportar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportar2);

        repo = new Repositorio(this);
        iv_diagrama = findViewById(R.id.iv_diagramaExp);
        button1=(Button)findViewById(R.id.button1);
        tv_infoExportar= findViewById(R.id.tv_infoExportar);

        nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
        exportarProyecto(nombreProyecto);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent();
                setResult(2,intent);
                finish();//finishing activity
            }
        });

    }

    public void exportarProyecto(String nombreProyecto) {
        Proyecto proy = repo.getProyecto(nombreProyecto);
        this.proyId = proy.getId();


        crearCarpetaInicial();

        for (String diagrama: proy.getDiagramas()) {
            marcarRelevamientos(diagrama);
        }

        exportarElementos(proyId);
        exportarFormularios(proyId);

        controlarVista();
    }

    private void controlarVista() {
        button1.setEnabled(true);
        tv_infoExportar.setText("Se exportaron los datos exitosamente");

    }

    private void crearCarpetaInicial() {
        File f = new File(Environment.getExternalStorageDirectory(), nombreProyecto);
        if (!f.exists()) {
            f.mkdirs();
        }
    }


    private void marcarRelevamientos(String pathDiagrama){
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
        exportarDiagrama(mutableBitMap, pathDiagrama); //genera una imagen a partir del bitmap

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
        String nombreDiagrama = cortarNombreDiagramas(pathDiagrama);
        File file = new File (f, nombreDiagrama);
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

    private String cortarNombreDiagramas( String pathDiagramas){
        String substring = pathDiagramas.substring(21); //elimina ""mnt/sdcard/Pictures/""
        return  substring;
    }

    private void exportarElementos(int proyId) {
        File carpeta = new File(Environment.getExternalStorageDirectory(), nombreProyecto);
        String archivoAgenda = carpeta.toString() + "/" + "elementosExportados.csv";

        int formId, correctitud;
        ArrayList<Elemento> listaElementos = repo.getElementos(proyId);
        try {
            FileWriter fileWriter = new FileWriter(archivoAgenda);
            // encabezado tabla
            fileWriter.append("Nombre Elemento");
            fileWriter.append(";");
            fileWriter.append("Correctitud");
            fileWriter.append(";");
            fileWriter.append("Num de Formulario");
            fileWriter.append("\n");

            for (Elemento e : listaElementos) {
                fileWriter.append("" + e.getNombre());
                fileWriter.append(";");

                correctitud = -1;
                formId = e.getFormId();
                if (formId != 0) { //tiene formulario asociado
                    correctitud = getCorrectitudFormulario(formId); // 1 o 0
                    if (correctitud == 1) {
                        fileWriter.append("correcto");
                    } else {
                        fileWriter.append("incorrecto");
                    }
                }else {
                    fileWriter.append("no relevado");
                }
                fileWriter.append(";");

                fileWriter.append(""+formId);
                fileWriter.append("\n");

            }
            fileWriter.close();
        } catch (Exception exc) { }

    }

    private int getCorrectitudFormulario(int formId) { return repo.getCorrectitudFormulario(formId); }

    private void exportarFormularios(int proyId) {
        File carpeta = new File(Environment.getExternalStorageDirectory(), nombreProyecto);
        String archivoAgenda = carpeta.toString() + "/" + "formulariosExportados.csv";

        Formulario form;
        ArrayList<Integer> todosIdForm = repo.getIdFormulariosDeProyecto(proyId);

        try {
            FileWriter fileWriter = new FileWriter(archivoAgenda);
            // encabezado tabla
            fileWriter.append("Num de Formulario");
            fileWriter.append(";");
            fileWriter.append("Diagrama");
            fileWriter.append(";");
            fileWriter.append("Fotos");
            fileWriter.append(";");
            fileWriter.append("Audios");
            fileWriter.append("\n");

            for (Integer id: todosIdForm) {
                form = repo.getFormulario(id);

                fileWriter.append("" +form.getId());
                fileWriter.append(";");

                String nombreDiagrama = cortarNombreDiagramas(form.getDiagrama());
                fileWriter.append("" +nombreDiagrama);
                fileWriter.append(";");

                String fotos = convertListToString(form.getFotos());
                fileWriter.append(fotos);
                fileWriter.append(";");

                String audios = convertListToString(form.getAudios());
                fileWriter.append(audios);
                fileWriter.append("\n");
            }
            fileWriter.close();
        } catch (Exception exc) { }
    }



}
