package com.example.relevamiento.modelos;

import java.util.ArrayList;

public class Formulario {
    private int id;
    private String diagrama; //direccion a memoria del dispositivo
    private ArrayList<Integer> marcas; //[x1],[y1],[x2],[y2], ...,
    private boolean esCorrecto;
    private ArrayList<String> fotos;
    private ArrayList<String> audios;

    public Formulario( int id, String diag, ArrayList<Integer> m, boolean correct) {
        this.id = id;
        diagrama = diag;
        esCorrecto = correct;
        marcas = m;
        audios = new ArrayList<>();
        fotos = new ArrayList<>();
    }


    public void agregarFoto(String f) {
        fotos.add(f);
    }
    public void agregarAudio(String a) {
        audios.add(a);
    }
    public void setMarcas(ArrayList<Integer> marcas){
        this.marcas = marcas;
    }

    public void setEsCorrecto(boolean esCorrecto) {
        this.esCorrecto = esCorrecto;
    }

    public int getId() {
        return id;
    }

    public String getDiagrama() {
        return diagrama;
    }

    public ArrayList<Integer> getMarcas() {
        return marcas;
    }

    public boolean isEsCorrecto() {
        return esCorrecto;
    }

    public ArrayList<String> getFotos() {
        return fotos;
    }

    public ArrayList<String> getAudios() {
        return audios;
    }





}

