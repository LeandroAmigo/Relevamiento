package com.example.relevamiento.modelos;

import java.util.ArrayList;

public class Elemento {

    private String nombre;
    private Formulario formulario;

    public Elemento (String name){
        nombre = name;
        formulario = null;
    }

    public void setFormulario(Formulario form) { formulario = form; }

    public String getNombre() {
        return nombre;
    }
    public Formulario getFormulario() {
        return formulario;
    }



}
