package com.waifus.servlets;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.model.LikeThread;
import com.waifus.model.Thread;
import com.waifus.model.User;
import com.waifus.services.JWTService;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class LikeThreadServlet extends HttpServlet {
    private Properties prop;

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
        String dateLike = req.getParameter("dateLike");
        ResponseService<User> responseService = new ResponseService<User>();
        Thread thread = new Thread(Integer.parseInt(req.getParameter("idThread")));
        String jwt = req.getHeader("Authorization");
        try {
            DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
            LikeThread likeThread = new LikeThread(thread.getIdThread(), Integer.parseInt(String.valueOf(decodedJWT.getClaim("idUser"))), dateLike);
            likeThread.add();
            thread = thread.get();
            User userThread = new User(thread.getUser());
            userThread.karmaChange(1);
            JsonObject json = new JsonObject();
            json.add("like", new JsonPrimitive("ok"));
            responseService.outputResponse(resp, json.toString(), 200);
        }catch (SQLException e){
            System.out.println(prop.getProperty("error.db"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.db")), 400);
        }catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }
    }
}
