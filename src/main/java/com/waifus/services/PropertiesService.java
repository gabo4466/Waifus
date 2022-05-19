package com.waifus.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesService {
    private PropertiesService(){
    }

    /**
     * Carga el fichero de properties con los literales
     * @param file nombre del fichero
     * @return el properties cargado
     */
    public static Properties getProperties(String file){
        InputStream input = PropertiesService.class.getClassLoader().getResourceAsStream(file+".properties");
        Properties prop = new Properties();
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

}
