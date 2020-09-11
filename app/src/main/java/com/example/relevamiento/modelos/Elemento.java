package com.example.relevamiento.modelos;

import java.util.ArrayList;

public class Elemento {

    private int id;
    private String nombre;

    public Elemento (int id, String name){
        this.id = id;
        nombre = name;
    }


    public String getNombre() {
        return nombre;
    }
    public int getId() {
        return id;
    }



}
