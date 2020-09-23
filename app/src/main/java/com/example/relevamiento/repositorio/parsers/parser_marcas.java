package com.example.relevamiento.repositorio.parsers;

import java.util.ArrayList;

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
    public static ArrayList<Integer> convertStringToList(String str){
        String[] arr = str.split(Separator);
        ArrayList<Integer> salida = new ArrayList<Integer>(arr.length);
        for (String s: arr){
            salida.add(Integer.parseInt(s));
        }
        return salida;
    }


}
