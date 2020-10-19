package com.example.relevamiento.repositorio.parsers;

import java.util.ArrayList;

public class parser_audiosYfotos {

    public static String Separator = "_,_";

    public static String convertListToString(ArrayList<String> array){
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
    public static ArrayList<String> convertStringToList(String str){
        String[] arr = str.split(Separator);
        ArrayList<String> salida = new ArrayList<>(arr.length);
        for (String s: arr){
            salida.add(s);
        }
        return salida;
    }


}
