package com.example.relevamiento.modelos;

public class Proyecto {

    private String nombre;
    private String ubicacion_elementos;
    private String ubicacion_diagramas;
    private boolean permite_fotos;

    public Proyecto(String nombre, String ubicacion_diagramas, String ubicacion_elementos, boolean permite_fotos) {
        this.nombre = nombre;
        this.ubicacion_elementos = ubicacion_elementos;
        this.ubicacion_diagramas = ubicacion_diagramas;
        this.permite_fotos = permite_fotos;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacion_elementos() {
        return ubicacion_elementos;
    }

    public String getUbicacion_diagramas() {
        return ubicacion_diagramas;
    }

    public boolean Permite_fotos() {
        return permite_fotos;
    }

    public void setUbicacion_elementos(String ubicacion_elementos) {
        this.ubicacion_elementos = ubicacion_elementos;
    }

    public void setUbicacion_diagramas(String ubicacion_diagramas) {
        this.ubicacion_diagramas = ubicacion_diagramas;

    }

    public void setPermite_fotos(boolean permite_fotos) {
        this.permite_fotos = permite_fotos;
    }

}
