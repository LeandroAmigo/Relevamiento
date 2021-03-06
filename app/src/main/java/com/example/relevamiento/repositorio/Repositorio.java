package com.example.relevamiento.repositorio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.relevamiento.db.DataBaseHelper;
import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.modelos.Formulario;
import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.parsers.parser_audiosYfotos;
import com.example.relevamiento.repositorio.parsers.parser_diagramas;
import com.example.relevamiento.repositorio.parsers.parser_elementos;
import com.example.relevamiento.repositorio.parsers.parser_marcas;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class Repositorio {

    private DataBaseHelper helper;
    private SQLiteDatabase BaseDeDatos;

    public Repositorio(Context context){
        helper = DataBaseHelper.getInstance(context);
    }

    private void open(){
        BaseDeDatos = helper.getWritableDatabase();
    }

    private void close() {
        BaseDeDatos.close();
    }

    public ArrayList<String> getProyectos() {
        ArrayList<String> salida = new ArrayList<String>();
        open();
        Cursor c = BaseDeDatos.rawQuery("select proyecto_nombre from proyectos", null);
        if (c.moveToFirst()) {
            do {
                salida.add(c.getString(0)); //"select 0,1,2,3... from proyectos"
            } while (c.moveToNext());
        }
        close();
        return salida;
    }

    public Proyecto getProyecto(String nombreProyecto) {
        Proyecto salida = null;
        int proyId = -1;
        String nombre_existente = null;
        String diagramas_existente = null;
        int permite_fotos_existente = 0;

        open();
        Cursor c = BaseDeDatos.rawQuery
                ("select proyecto_id, proyecto_nombre, proyecto_diagramas, proyecto_permiteFotos from proyectos where proyecto_nombre = '" +nombreProyecto+ "'", null);

        if (c.moveToFirst()) {
            proyId = c.getInt(0);
            nombre_existente = c.getString(1);
            diagramas_existente = c.getString(2);
            permite_fotos_existente = c.getInt(3);
        }
        close();

        //parsear diagramas
        String[] diagramas = parser_diagramas.convertStringToArray(diagramas_existente);
        ArrayList<String> listaDiagramas = new ArrayList<String> (Arrays.asList(diagramas));
        //parsear permiteFoto
        boolean pf = false;
        if (permite_fotos_existente == 1)
            pf = true;
        //crear Proyecto
        salida = new Proyecto(proyId, nombre_existente, listaDiagramas, pf);
        return salida;
    }

    public int getIdProyecto(String nombreProyecto){
        int salida = -1;
        open();
        Cursor c = BaseDeDatos.rawQuery
                ("select proyecto_id from proyectos where proyecto_nombre = '" + nombreProyecto + "'", null);
        if (c.moveToFirst()) {
            salida = (c.getInt(0)); //"select 0,1,2,3... from proyectos"
        }
        close();
        return salida;
    }

    public boolean crearProyecto(String nombreProyecto, String[] pathDiagramas, String pathElementos, boolean permiteFoto){
        String diagramas = parser_diagramas.convertArrayToString(pathDiagramas);
        int permite_fotos = 0;
        if (permiteFoto) {
            permite_fotos = 1;
        }
        ContentValues cv = new ContentValues();
        cv.put("proyecto_nombre",nombreProyecto);
        cv.put("proyecto_diagramas", diagramas);
        cv.put("proyecto_permiteFotos", permite_fotos);

        open();
            long result = BaseDeDatos.insert("proyectos", null, cv);
        close();

        if (pathElementos != null) {
            int proyId = getIdProyecto(nombreProyecto);
            agregarElementos(proyId, pathElementos);
        }

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean actualizarPermiteFotosProyecto(int proyId, boolean permiteFoto) {
        boolean exito = false;
        int pf = 0;
        if (permiteFoto)
            pf = 1;
        ContentValues cv = new ContentValues();
        cv.put("proyecto_permiteFotos", pf);
        open();
            int cant = BaseDeDatos.update("proyectos", cv, "proyecto_id =" +proyId, null);
        close();
        if (cant == 1)
            exito = true;
        return exito;
    }

    public boolean actualizarDiagramasProyecto(int proyId, ArrayList<String> existentes, String[] pathDiagramasParaAgregar) {
        boolean exito = false;
        //controlar que no haya repetidos
        for (String s: pathDiagramasParaAgregar){
             if (!existentes.contains(s)){
                 existentes.add(s);
             }
        }
        //convertir "existentes" a arreglo
        String[] diagramasActualizados = existentes.toArray(new String[0]);
        //pasar a string para guardar en BD (TEXT)
        String diagramas = parser_diagramas.convertArrayToString(diagramasActualizados);
        ContentValues cv = new ContentValues();
        cv.put("proyecto_diagramas", diagramas);
        open();
            int cant = BaseDeDatos.update("proyectos", cv, "proyecto_id =" +proyId, null);
        close();
        if (cant == 1)
            exito = true;
        return exito;
    }

    public boolean actualizarNombreProyecto(int proyId, String nombreProyecto) {
        boolean exito = false;
        ContentValues cv = new ContentValues();
        cv.put("proyecto_nombre",nombreProyecto);
        open();
            int cant = BaseDeDatos.update("proyectos", cv, "proyecto_id =" +proyId, null);
        close();
        if (cant == 1)
            exito = true;
        return exito;
    }

    public void agregarElementos(int proyId, String pathElementos){
        ArrayList<String> listaElementos = parser_elementos.obtenerListaNombresElementos(pathElementos);
        ContentValues cv = new ContentValues();
        open();
        for (int i=0; i< listaElementos.size(); i++){
            cv.put("elemento_nombre", listaElementos.get(i));
            cv.put("proyecto_id", proyId);
            BaseDeDatos.insert("elementos", null, cv);

        }
        close();
    }

    public void agregarNuevoElemento(int proyId, String nombre){
        ContentValues cv = new ContentValues();
        open();
            cv.put("elemento_nombre", nombre);
            cv.put("proyecto_id", proyId);
            BaseDeDatos.insert("elementos", null, cv);

        close();
    }

    public ArrayList<Elemento> getElementos (int proyId){
        ArrayList<Elemento> salida = new ArrayList<>();
        open();
        Cursor c = BaseDeDatos.rawQuery("select elemento_id, elemento_nombre, formulario_id from elementos where proyecto_id =" + proyId, null);
        if (c.moveToFirst()) {
            do {
                salida.add (new Elemento (c.getInt(0), c.getString(1), c.getInt(2)) );
            } while (c.moveToNext());
        }
        close();
        return salida;
    }

    public int getIdElemento(String nombre, int proyId){
        int salida = -1;
        open();
        Cursor c = BaseDeDatos.rawQuery
                ("select elemento_id from elementos where elemento_nombre = '" + nombre + "' and proyecto_id = "+proyId, null);
        if (c.moveToFirst()) {
            salida = (c.getInt(0)); //"select 0,1,2,3... from .."
        }
        close();
        return salida;
    }

    public boolean actualizarElemento(int elemId, int formId){
        boolean exito = false;
        ContentValues cv = new ContentValues();
        cv.put("formulario_id", formId);
        open();
        int cant = BaseDeDatos.update("elementos", cv, "elemento_id = " +elemId, null);
        close();
        if (cant == 1)
            exito = true;
        return exito;
    }



    public ArrayList<Formulario> getFormularios (String pathDiagrama, int proyId){
        ArrayList<Formulario> salida = new ArrayList<Formulario>();
        int formId;
        String marcas_existentes;
        int esCorrecto_existente;
        boolean correcto;
        ArrayList<Integer> marcas;
        open();
        Cursor c = BaseDeDatos.rawQuery
                ("select formulario_id, formulario_marcas, formulario_correcto from formularios where proyecto_id =" +proyId+" and formulario_diagrama = '" +pathDiagrama+ "'", null);
        if (c.moveToFirst()) {
            do {
                formId = c.getInt(0);

                marcas_existentes = c.getString(1);
                marcas = parser_marcas.convertStringToList(marcas_existentes);

                correcto = false;
                esCorrecto_existente = c.getInt(2);
                if (esCorrecto_existente == 1)
                    correcto = true;

                salida.add (new Formulario (formId, pathDiagrama, marcas , correcto) );
            } while (c.moveToNext());
        }
        close();
        return salida;
    }


    public Formulario getFormulario (int formId){
        Formulario salida;
        String diagrama = null, marcas_existentes = null, fotos_existentes = null, audios_existentes = null;
        int esCorrecto_existente = 0;
        boolean correcto = false;
        open();
        Cursor c = BaseDeDatos.rawQuery
                ("select formulario_diagrama, formulario_marcas, formulario_correcto, formulario_foto, formulario_audio from formularios where formulario_id =" +formId, null);
        if (c.moveToFirst()) {
            diagrama = c.getString(0);
            marcas_existentes = c.getString(1);
            esCorrecto_existente = c.getInt(2);
            fotos_existentes = c.getString(3);
            audios_existentes = c.getString(4);
        }
        close();
        //parsear marcas
        ArrayList<Integer> marcas = parser_marcas.convertStringToList(marcas_existentes);
        //parsear correctitud
        if (esCorrecto_existente == 1)
            correcto = true;

        salida = new Formulario (formId, diagrama, marcas , correcto);
        if (fotos_existentes != null) {
            ArrayList<String> fotos = parser_audiosYfotos.convertStringToList(fotos_existentes);
            for (String foto: fotos){
                salida.agregarFoto(foto);
            }
        }
        if (audios_existentes != null){
            ArrayList<String> audios = parser_audiosYfotos.convertStringToList(audios_existentes);
            for (String audio: audios){
                salida.agregarAudio(audio);
            }
        }
        return salida;
    }


    public int getCorrectitudFormulario ( int formId){
        int salida = -1;
        open();
        Cursor c = BaseDeDatos.rawQuery
                ("select formulario_correcto from formularios where formulario_id =" +formId, null);
        if (c.moveToFirst()) {
             salida = c.getInt(0);
        }
        close();
        return salida;
    }

    public int crearFormulario(int proyId, String diagrama, ArrayList<Integer> listaMarcas, boolean correctitud){
        String marcas = parser_marcas.convertListToString(listaMarcas);
        int formulario_correcto = 0;
        if (correctitud) {
            formulario_correcto = 1;
        }
        ContentValues cv = new ContentValues();
        cv.put("formulario_diagrama",diagrama);
        cv.put("formulario_marcas", marcas);
        cv.put("formulario_correcto", formulario_correcto);
        cv.put("proyecto_id", proyId);

        open();
        long result = BaseDeDatos.insert("formularios", null, cv);
        close();

        return (int) result;
    }

    public boolean agregarAudioFormulario(int formId, ArrayList<String> listaAudios){
        boolean exito = false;
        String str_audios = parser_audiosYfotos.convertListToString(listaAudios);
        ContentValues cv = new ContentValues();
        cv.put("formulario_audio", str_audios);
        open();
        int cant = BaseDeDatos.update("formularios", cv, "formulario_id =" +formId, null);
        close();
        if (cant == 1)
            exito = true;

        return exito;
    }

    public boolean agregarFotoFormulario(int formId, ArrayList<String> listaFotos){
        boolean exito = false;
        String str_fotos = parser_audiosYfotos.convertListToString(listaFotos);
        ContentValues cv = new ContentValues();
        cv.put("formulario_foto", str_fotos);
        open();
        int cant = BaseDeDatos.update("formularios", cv, "formulario_id =" +formId, null);
        close();
        if (cant == 1)
            exito = true;

        return exito;
    }

    public void exportarDiagrama(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Diagramas_Relevados");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String fname = "Image-"+ currentDateandTime +".jpg";
        File file = new File (myDir, fname);
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


    public int getFormId(String elem, int proyId) {
        int salida = -1;
        open();
        Cursor c = BaseDeDatos.rawQuery
                ("select formulario_id from elementos where elemento_nombre = '" + elem + "' and proyecto_id = "+proyId, null);
        if (c.moveToFirst()) {
            salida = (c.getInt(0)); //"select 0,1,2,3... from .."
        }
        close();
        return salida;
    }

    public ArrayList<String> getElementosMismoFormulario(int proyId, int formId) {
        ArrayList<String> salida = new ArrayList<>();
        open();
        Cursor c = BaseDeDatos.rawQuery("select elemento_nombre from elementos where proyecto_id =" + proyId +" and formulario_id = " +formId, null);
        if (c.moveToFirst()) {
            do {
                salida.add (c.getString(0));
            } while (c.moveToNext());
        }
        close();
        return salida;
    }

    public boolean actualizarFormulario(Formulario formulario) {
        boolean exito = false;
        String str_marcas = parser_marcas.convertListToString(formulario.getMarcas());
        int formulario_correcto = 0;
        if (formulario.isEsCorrecto()) {
            formulario_correcto = 1;
        }
        String str_audios = parser_audiosYfotos.convertListToString(formulario.getAudios());
        String str_fotos = parser_audiosYfotos.convertListToString(formulario.getFotos());

        ContentValues cv = new ContentValues();
        cv.put("formulario_marcas", str_marcas);
        cv.put("formulario_correcto",formulario_correcto );
        cv.put("formulario_foto", str_fotos);
        cv.put("formulario_audio",str_audios);

        open();
        int cant = BaseDeDatos.update("formularios", cv, "formulario_id = " +formulario.getId(), null);
        close();

        if (cant == 1)
            exito = true;
        return exito;
    }

    public boolean eliminarFormulario(int formId) {
        open();
            long result = BaseDeDatos.delete("formularios", "formulario_id = " +formId, null);
        close();
        if(result == -1) {
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<Integer> getIdFormulariosDeProyecto(int proyId) {
        ArrayList<Integer> salida = new ArrayList<Integer>();
        open();
        Cursor c = BaseDeDatos.rawQuery("select formulario_id from formularios where proyecto_id =" +proyId, null);
        if (c.moveToFirst()) {
            do {
                salida.add(c.getInt(0)); //"select 0,1,2,3... from proyectos"
            } while (c.moveToNext());
        }
        close();
        return salida;
    }

}