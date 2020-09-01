package com.example.relevamiento.modelos;

import java.util.ArrayList;

public class Elemento {

    private String nombre;
    private ArrayList<Float> marcas; //[x1],[y1],[x2],[y2], ...,
    private boolean esCorrecto;
    private Formulario formulario;
    private String diagrama; //direccion a memoria del dispositivo

    public Elemento (String name, int num){
        nombre = name;
        marcas = null;
        esCorrecto = false;
        formulario = null;
        diagrama = null;
    }

    public String getNombre() {
        return nombre;
    }
    public ArrayList<Float> getMarcas() {
        return marcas;
    }
    public boolean isEsCorrecto() {
        return esCorrecto;
    }
    public Formulario getFormulario() {
        return formulario;
    }
    public String getDiagrama() {
        return diagrama;
    }


    public void setMarcas(ArrayList<Float> marcas) {
        this.marcas = marcas;
    }

    public void addMarca(float marca) {
        this.marcas = marcas;
    }

    public void setEsCorrecto(boolean esCorrecto) {
        this.esCorrecto = esCorrecto;
    }
    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }
    public void setDiagrama(String diagrama) {
        this.diagrama = diagrama;
    }
}
