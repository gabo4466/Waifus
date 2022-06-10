package com.waifus.servlets;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.exceptions.ThreadException;
import com.waifus.services.JWTService;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;
import com.waifus.model.Thread;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class ThreadCreationServlet extends HttpServlet {
    private Properties prop;

    @Override
    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jwt = req.getHeader("Authorization");
        ResponseService<Thread> responseService = new ResponseService<Thread>();
        Thread thread = new Gson().fromJson(req.getReader(), Thread.class);
        try{
            DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
            thread.setUser(Integer.parseInt(String.valueOf(decodedJWT.getClaim("idUser"))));
            thread.setChannel(Integer.parseInt(req.getParameter("idChannel")));
            thread=thread.add();
            JsonObject json = new JsonObject();
            json.add("added", new JsonPrimitive("ok"));
            json.add("thread", new Gson().toJsonTree(thread));
            responseService.outputResponse(resp, json.toString(), 200);
        }catch (ThreadException e){
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(e.getMessage()), 400);
        }catch (SQLException e){
            System.out.println(prop.getProperty("error.db"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.db")), 400);
        }catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(e.getMessage()), 400);
        }
    }
}
