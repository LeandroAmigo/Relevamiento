package com.example.relevamiento.repositorio.parsers;

import java.util.ArrayList;
import java.util.LinkedList;

public class parser_marcas {

    public static String Separator = "_,_";

    public static String convertListToString(ArrayList<Integer> array){
        String str = "";
        for (int i=0; i<array.size(); i++) {
            str = str+array.get(i);
            // Do not append comma at the end of last element
            if(i<array.size()-1){
                str = str+Separator;
            }
        }
        return str;
    }
    public static ArrayList<Float> convertStringToList(String str){
        String[] arr = str.split(Separator);
        ArrayList<Float> salida = new ArrayList<Float>(arr.length);
        for (String s: arr){
            salida.add(Float.parseFloat(s));
        }
        return salida;
    }

    public static void ordenarMarcas (ArrayList<Float> marcas){  // x1 < x2 // y1 < y2
        float x1 = marcas.get(0);
        float y1 = marcas.get(1);
        float x2 = marcas.get(2);
        float y2 = marcas.get(3);




    }

}
