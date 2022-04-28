package com.waifus.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.waifus.daoImp.UserDaoImp;
import com.waifus.model.User;
import com.waifus.services.ResponseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

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
        /*
        System.out.println("Email: "+user.getEmail());
        System.out.println("HOla soy el post y te envio eljson");
        user.setAdmin(true);
        user.setBirthday(new Date(1999, Calendar.JUNE,4));

        responseService.outputResponse(resp, responseService.toJson(user), 200);

        System.out.println("HOla soy el post y ya te envie el json");*/
        try{
            UserDaoImp userDaoImp = new UserDaoImp();
            userDaoImp.logIn(user);
        }catch (Exception e){
            System.out.println("No se ha podido establecer conexion con la base de datos.");
            System.out.println(e.getMessage());
        }


    }
}
