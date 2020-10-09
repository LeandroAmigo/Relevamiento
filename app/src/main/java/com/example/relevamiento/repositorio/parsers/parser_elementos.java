package com.example.relevamiento.repositorio.parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class parser_elementos {

    public static String separador = ";";

    public static ArrayList<String> obtenerListaNombresElementos(String pathElementos) {
        ArrayList<String> salida = new ArrayList<>();
        String line = "";
        String nombreElemento ="";
        String[] data;
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(pathElementos));
            //csvReader.readLine();  //si hay encabezado
            while ((line = csvReader.readLine()) != null) {
                // separar cada columna por "separador"
                data = line.split(separador);
                nombreElemento = data[0]; //el nombre es la primer columna del excel. las demas no importan
                salida.add(nombreElemento);
            }
            csvReader.close();
        } catch (IOException e) {
        }
        return salida;
    }

}
