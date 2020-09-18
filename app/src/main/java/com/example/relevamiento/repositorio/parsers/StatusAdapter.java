package com.example.relevamiento.repositorio.parsers;

public class StatusAdapter {

    private String nombreElemento;
    private int correcto;

    public StatusAdapter(String nombreElemento, int correcto) {
        this.nombreElemento = nombreElemento;
        this.correcto = correcto;
    }

    public String getNombreElemento() {
        return nombreElemento;
    }


    public int getCorrecto() {
        return correcto;
    }




}
