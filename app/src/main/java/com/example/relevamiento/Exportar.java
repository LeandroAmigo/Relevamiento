package com.example.relevamiento;

import android.content.Context;

import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.modelos.Proyecto;
import com.example.relevamiento.repositorio.Repositorio;

import java.util.ArrayList;

public class Exportar {
    private Context context;
    private Repositorio repo;

    public Exportar (Context context){
        this.context = context;
        repo = new Repositorio(context);
    }

    public void exportarProyecto(String nombreProyecto) {
        int formId, correctitud;
        Proyecto proy = repo.getProyecto(nombreProyecto);
        ArrayList<Elemento> listaElementos = repo.getElementos(proy.getId());
        for (Elemento e : listaElementos) {
            formId = e.getFormId();
        }
    }


}
