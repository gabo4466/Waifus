package com.waifus.services;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

public class ResponseService<G> {

    public ResponseService() {
    }

    /**
     * Metodo que asigna la informacion de respuesta en la respuesta http
     * @param response Respuesta del protocolo Http
     * @param payload Data de respuesta
     * @param status Estado de la respuesta segun el protocolo Http
     */
    public void outputResponse(HttpServletResponse response, String payload, int status){
        response.setHeader("Content-Type", "application/json");
        try{
            response.setStatus(status);
            if (payload != null){
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(payload.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un String con formato Json del objeto
     * @return Un string que contiene el Json del objeto
     */
    public String toJson(G obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * Metodo que recibe una palabra y la hashea con el tipo SHA-256
     * @param value Palabra que se desea hashear
     * @return Palabra hasheada
     */
    public String toHash(String value){
        String result;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            byte[] digest = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : digest) {
                hexString.append(Integer.toHexString(0xFF & b));
            }
            result = hexString.toString();
        }catch (Exception e){
            System.out.println("Hubo un fallo al hashear la palabra");
            result = "";
        }
        return result;
    }
}
