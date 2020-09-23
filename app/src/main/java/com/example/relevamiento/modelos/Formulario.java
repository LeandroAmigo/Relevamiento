package com.example.relevamiento.modelos;

import java.util.ArrayList;

public class Formulario {
    private int id;
    private String diagrama; //direccion a memoria del dispositivo
    private ArrayList<Integer> marcas; //[x1],[y1],[x2],[y2], ...,
    private boolean esCorrecto;
    private String observacion;
    private String foto;
    private String audio;
    private String imagen;

    public Formulario( int id, String diag, ArrayList<Integer> m, boolean correct) {
        this.id = id;
        diagrama = diag;
        esCorrecto = correct;
        marcas = m;
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


    public int getId() { return id; }
    public String getDiagrama() {
        return diagrama;
    }

    public ArrayList<Integer> getMarcas() {
        return marcas;
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

