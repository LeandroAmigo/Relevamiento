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

    /*public Proyecto getProyecto(String nombre) {
        Proyecto salida = null;
        String pathDiagramas = null;
        int permite_fotos = 0;
        open();
        Cursor c = BaseDeDatos.rawQuery
                ("select proyecto_id, proyecto_nombre, proyecto_diagramas, proyecto_permiteFotos from proyectos where proyecto_nombre = " + nombre , null);
        if (c.moveToFirst()) {
                salida.setId(c.getInt(0)); //"select 0,1,2,3... from proyectos"
                salida.setNombre(c.getString(1));
                pathDiagramas = c.getString(2);
                permite_fotos = c.getInt(3);

            if (pathDiagramas != null){ //convertir de Paths concatenados a arreglo de strings y luego a lista de string (path)
                ArrayList<String> diagramas = (ArrayList<String>) Arrays.asList(parser_diagramas.convertStringToArray(pathDiagramas));
                salida.setDiagramas(diagramas);
            }
            boolean pf;
            if (permite_fotos == 0)
                pf = true;
            else
                pf = false;
            salida.setPermite_fotos(pf);
        }

        close();

        return salida;
    }
     */

    public int getIdProyecto(String nombreProyecto){
        int salida = -1;
        open();
        Cursor c = BaseDeDatos.rawQuery
                ("select proyecto_id from proyectos where proyecto_nombre = " + nombreProyecto, null);
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
            ArrayList<String> listaElementos = parser_elementos.obtenerListaNombresElementos(pathElementos);
            int proyId = getIdProyecto(nombreProyecto);
            if (proyId != -1){
                agregarElementos(proyId, listaElementos);
            }
        }

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean actualizarProyecto(int proyId, String nombreProyecto, String[] pathDiagramas, String pathElementos, boolean permiteFoto){
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
        int cant = BaseDeDatos.update("proyectos", cv, "proyecto_id =" +proyId, null);
        close();

        Log.e("PROYECTO", nombreProyecto +" "+ diagramas +" "+ permite_fotos );

        if (pathElementos != null) {
            ArrayList<String> listaElementos = parser_elementos.obtenerListaNombresElementos(pathElementos);
            if (cant == 1) {
                agregarElementos(proyId, listaElementos);
            }
        }

        if (cant == 1)
            return true;
        else
            return false;
    }

    private void agregarElementos(int proyId, ArrayList<String> listaElementos){
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


}
