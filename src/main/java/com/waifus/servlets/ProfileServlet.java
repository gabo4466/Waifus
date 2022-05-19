package com.waifus.servlets;

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

public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<User> responseService = new ResponseService<User>();
        int i = 1;
        User user = new User(i);
        try{
            user.get();
            String jsonUser = responseService.toJson(user);
            responseService.outputResponse(resp, jsonUser,200);
        }catch (Exception e){

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }
}
