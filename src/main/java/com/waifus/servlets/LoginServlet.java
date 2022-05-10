package com.waifus.servlets;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.exceptions.UserException;
import com.waifus.model.User;
import com.waifus.services.ResponseService;
import com.waifus.services.SecurityService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // mucho texto
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<User> responseService = new ResponseService<User>();
        User user = new Gson().fromJson(req.getReader(), User.class);
        user.setPassword(responseService.toHash(user.getPassword()));
        try{
            User userLogged = user.logIn();
            String jwt = SecurityService.createJWT(userLogged);
            JsonObject json = new JsonObject();
            json.add("jwt",new JsonPrimitive(jwt));
            responseService.outputResponse(resp, json.toString(), 200);
        }catch (UserException e){
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(e.getMessage()), 200);
        }catch (SQLException e){
            System.out.println("No se ha podido establecer conexion con la base de datos.");
            responseService.outputResponse(resp, responseService.errorResponse(e.getMessage()), 200);
        }catch (Exception e){
            System.out.println("Ha ocurrido algún error");
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(e.getMessage()), 200);
        }


    }
}
