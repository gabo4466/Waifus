package com.waifus.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.exceptions.UserException;
import com.waifus.exceptions.UserNotFoundException;
import com.waifus.model.User;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;
import com.waifus.services.SecurityService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class LoginServlet extends HttpServlet {

    private Properties prop;

    @Override
    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        // mucho texto
        ResponseService<User> responseService = new ResponseService<User>();
        responseService.outputResponse(resp, "{\"prueba\":\"Good\"}", 200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<User> responseService = new ResponseService<User>();
        User user = new Gson().fromJson(req.getReader(), User.class);
        user.setPassword(responseService.toHash(user.getPassword()));
        User userLogged = null;
        try {
            userLogged = user.logIn();
            String jwt = SecurityService.createJWT(userLogged);
            JsonObject json = new JsonObject();
            json.add("access", new JsonPrimitive(jwt));
            json.add("user", new Gson().toJsonTree(userLogged));
            responseService.outputResponse(resp, json.toString(), 200);
        }catch (UserException e){
            JsonObject json = new JsonObject();
            json.add("resp", new JsonPrimitive(e.getMessage()));
            user.setPassword(null);
            json.add("user", new Gson().toJsonTree(user));
            responseService.outputResponse(resp, json.toString(), 403);
        }catch (UserNotFoundException e){
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(e.getMessage()), 400);
        }catch (SQLException e){
            System.out.println(prop.getProperty("db.failed"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("resp.error")), 400);
        }catch (Exception e){
            System.out.println(prop.getProperty("resp.error"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("resp.error")), 400);
        }


    }
}
