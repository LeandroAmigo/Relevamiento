package com.example.relevamiento.modelos;

import java.util.ArrayList;

public class Formulario {
    private Proyecto proyecto;
    private String diagrama; //direccion a memoria del dispositivo
    private ArrayList<Float> marcas; //[x1],[y1],[x2],[y2], ...,
    private ArrayList<Elemento> elementos;
    private boolean esCorrecto;
    private String observacion;
    private String foto;
    private String audio;
    private String imagen;

    public Formulario( Proyecto p, String diag, ArrayList<Float> m, ArrayList<Elemento> elem, boolean correct) {
        proyecto = p;
        diagrama = diag;
        esCorrecto = correct;
        marcas = m;
        elementos = elem;
        observacion = null;
        audio = null;
        foto = null;
        imagen = null;
    }

    public void setObservacion(String obs) {
        observacion = obs;
    }
    public void setFoto(String f) {
        foto = f;
    }
    public void setAudio(String a) { audio = a; }
    public void setEsCorrecto(boolean correct) {
        esCorrecto = correct;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public String getDiagrama() {
        return diagrama;
    }

    public ArrayList<Float> getMarcas() {
        return marcas;
    }

    public ArrayList<Elemento> getElementos() {
        return elementos;
    }

    public boolean isEsCorrecto() {
        return esCorrecto;
    }

    public String getObservacion() {
        return observacion;
    }

    public String getFoto() {
        return foto;
    }

    public String getAudio() {
        return audio;
    }

    public String getImagen() {
        return imagen;
    }



}

