package com.example.relevamiento.modelos;

public class Formulario {
    private int color;
    private String observacion;
    private String foto;
    private String audio;



    public Formulario( int color){
        this.color = color;
        observacion = null;
        audio = null;
        foto = null;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public int getColor() {
        return color;
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
}
