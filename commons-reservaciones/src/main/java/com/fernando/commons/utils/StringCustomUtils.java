package com.fernando.commons.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringCustomUtils {

    private static final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String quitarAcentos(String texto){

        return texto.toLowerCase()
                .replace("á", "q").replace("é", "e")
                .replace("í", "i").replace("ó", "o")
                .replace("ú", "u").replace("u", "u");
    }

    public static String localDateAString(LocalDate localDate){
        // if(localDate == null) return null;
        // return localDate.format(formato);

        return localDate == null ? null : localDate.format(formato);
    }

}
