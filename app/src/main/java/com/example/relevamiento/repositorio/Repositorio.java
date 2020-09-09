package com.example.relevamiento.repositorio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.relevamiento.db.DataBaseHelper;
import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.parsers.parser_diagramas;
import com.example.relevamiento.repositorio.parsers.parser_elementos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                ("select proyecto_id, proyecto_nombre, proyecto_diagramas, proyecto_permiteFotos from proyectos where proyecto_nombre = '" + nombreProyecto + "'", null);

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
        if (permiteFoto == true) {
            permite_fotos = 1;
        }
        ContentValues cv = new ContentValues();
        cv.put("proyecto_nombre",nombreProyecto);
        cv.put("proyecto_diagramas", diagramas);
        cv.put("proyecto_permiteFotos", permite_fotos);

        open();
            long result = BaseDeDatos.insert("proyectos", null, cv);
        close();

        Log.e("PROYECTO", nombreProyecto +" "+ diagramas +" "+ permite_fotos );

        if (pathElementos != null) {
            int proyId = getIdProyecto(nombreProyecto);
            agregarElementos(proyId, pathElementos);
        }

        if(result == -1)
            return false;
        else
            return true;
    }

    public void agregarElementos(int proyId, String pathElementos){
        ArrayList<String> listaElementos = parser_elementos.obtenerListaNombresElementos(pathElementos);
        ContentValues cv = new ContentValues();
        open();
        for (int i=0; i< listaElementos.size(); i++){
            cv.put("elemento_nombre", listaElementos.get(i));
            cv.put("proyecto_id", proyId);
            BaseDeDatos.insert("elementos", null, cv);

            Log.e("ELEMENTO", listaElementos.get(i) +" "+ proyId );
        }
        close();
    }

    public boolean actualizarPermiteFotosProyecto(int proyId, boolean permiteFoto) {
        boolean exito = false;
        int pf = 0;
        if (permiteFoto == true)
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

    public boolean actualizarDiagramasProyecto(int proyId, String[] pathDiagramas) {
        boolean exito = false;
        String diagramas = parser_diagramas.convertArrayToString(pathDiagramas);
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
}