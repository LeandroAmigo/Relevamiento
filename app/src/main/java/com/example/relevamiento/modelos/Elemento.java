package com.example.relevamiento.modelos;

public class Elemento {

    private int id;
    private String nombre;
    private int formId;

    public Elemento (int id, String name){
        this.id = id;
        nombre = name;
        formId = -1;
    }


    public String getNombre() {
        return nombre;
    }
    public int getId() {
        return id;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public int getFormId() {
        return formId;
    }
}
