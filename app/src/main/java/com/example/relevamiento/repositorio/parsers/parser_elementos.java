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
                nombreElemento = data[0];
                salida.add(nombreElemento);
            }
            csvReader.close();
        } catch (IOException e) {
        }
        return salida;
    }

    public static String obtenerNombresElementos(String pathElementos) {
        String salida = null;
        StringBuilder sb = new StringBuilder();
        String line = "";
        String nombreElemento ="";
        String[] data;
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(pathElementos));
            //csvReader.readLine();  //si hay encabezado
            while ((line = csvReader.readLine()) != null) {
                // separar cada columna por coma ","
                data = line.split(separador);
                nombreElemento = data[0];
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(nombreElemento);
            }
            csvReader.close();
            salida = sb.toString();
        } catch (IOException e) {
        }
        return salida;
    }



}
