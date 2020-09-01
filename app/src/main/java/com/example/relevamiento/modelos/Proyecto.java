package com.example.relevamiento.modelos;

import java.util.ArrayList;


public class Proyecto {

    private String nombre;
    private ArrayList<String> diagramas; //lista de direcciones a memoria del dispositivo
    private ArrayList<Elemento> elementos;
    private boolean permite_fotos;


    public Proyecto() {    }


    public String getNombre() {
        return nombre;
    }

    public ArrayList<Elemento> getElementos() {
        return elementos;
    }

    public Elemento getElemento(int i) {
        if ( i>=0 && i<elementos.size() )
            return elementos.get(i);
        return null;
    }

    public String getDiagrama(String s) {
        String salida = null;
        for (String str: diagramas) {
            if (str.equals(s)){
                salida = str;
            }
        }
        return salida;
    }

    public ArrayList<String> getDiagramas() {
        return diagramas;

    }

    public boolean permite_fotos() {
        return permite_fotos;
    }

    public void setNombre(String nombre) {
        this.nombre= nombre;
    }

    public void setElementos(ArrayList<Elemento> e) {
        this.elementos = e;
    }

    public void setDiagramas(ArrayList<String> ubicacion_diagramas) {
        this.diagramas = ubicacion_diagramas;

    }

    public void setPermite_fotos(boolean permite_fotos) {
        this.permite_fotos = permite_fotos;
    }

}
