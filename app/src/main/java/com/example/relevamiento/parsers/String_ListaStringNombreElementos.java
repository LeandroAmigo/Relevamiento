package com.example.relevamiento.parsers;

import com.example.relevamiento.modelos.Elemento;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class String_ListaStringNombreElementos {

    public static ArrayList<String> obtenerListaElementos(String pathElementos) {
        ArrayList<String> salida = new ArrayList<>();
        String line = "";
        String nombreElemento ="";
        String[] data;
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(pathElementos));
            //csvReader.readLine();  //si hay encabezado
            while ((line = csvReader.readLine()) != null) {
                // separar cada columna por coma ","
                data = line.split(",");
                nombreElemento = data[0];
                salida.add(nombreElemento);
            }
            csvReader.close();
        } catch (IOException e) {
        }
        return salida;
    }


}
