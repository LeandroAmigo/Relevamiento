package com.example.relevamiento.modelos;

import java.util.ArrayList;


public class Proyecto {
    private int id;
    private String nombre;
    private ArrayList<String> diagramas; //lista de direcciones a memoria del dispositivo
    //private ArrayList<Elemento> elementos;
    //private ArrayList<Formulario> formularios;
    private boolean permite_fotos;

    public Proyecto(String n, ArrayList<String> d, boolean pf) {
        nombre = n;
        diagramas = d;
        permite_fotos = pf;
        //elementos = new ArrayList<Elemento>();
        //formularios = new ArrayList<Formulario>();

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String n) { nombre = n;}
    public void setPermite_fotos(boolean pf) {
        permite_fotos = pf;
    }

   /* public void agregarFormulario (Formulario f){
        formularios.add(f);
    }
    */
   public void setDiagramas(ArrayList<String> diagramas) {
       this.diagramas = diagramas;
   }


    public String getNombre() {
        return nombre;
    }
    public int getId() {
        return id;
    }
    /*public ArrayList<Elemento> getElementos() {
        return elementos;
    }

    public Elemento getElemento(int i) {
        if ( i>=0 && i<elementos.size() )
            return elementos.get(i);
        return null;
    }

     */
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
