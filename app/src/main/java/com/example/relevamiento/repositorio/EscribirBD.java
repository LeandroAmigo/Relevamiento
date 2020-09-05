package com.example.relevamiento.repositorio;

import android.database.sqlite.SQLiteDatabase;

import com.example.relevamiento.MainActivity;

public class EscribirBD {

    SQLiteDatabase BaseDeDatos = MainActivity.getBD().getWritableDatabase();

    public void guardarProyectoBD( ){}

    public void guardarElementoBD(){}

    public void guardarFormularioBD(){}

}
