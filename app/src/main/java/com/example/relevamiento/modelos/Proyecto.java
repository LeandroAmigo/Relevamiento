package com.example.relevamiento.modelos;

import java.util.ArrayList;


public class Proyecto {
    private int id;
    private String nombre;
    private ArrayList<String> diagramas; //lista de rutas a memoria del dispositivo
    private boolean permite_fotos;

    public Proyecto(int id, String n, ArrayList<String> d, boolean pf) {
        this.id = id;
        nombre = n;
        diagramas = d;
        permite_fotos = pf;
    }


    public void setNombre(String n) { nombre = n;}

    public void setPermite_fotos(boolean pf) {
        permite_fotos = pf;
    }

   public void setDiagramas(ArrayList<String> diagramas) {
       this.diagramas = diagramas;
   }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getDiagramas() {
        return diagramas;
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
    public boolean permite_fotos() {
        return permite_fotos;
    }




}
