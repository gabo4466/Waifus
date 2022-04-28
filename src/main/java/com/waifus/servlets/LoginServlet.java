package com.waifus.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.waifus.model.User;
import com.waifus.services.ResponseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonResponse = "{\"message\": \"Successs\"}";
        System.out.println("HOla soy el post");
        this.outputResponse(resp, jsonResponse, 200);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqBody;
        ResponseService<User> responseService = new ResponseService<User>();
        User obj = new Gson().fromJson(req.getReader(), User.class);
        obj.setPassword(responseService.toHash(obj.getPassword()));
        System.out.println("Email: "+obj.getEmail());
        System.out.println("HOla soy el post y te envio eljson");
        obj.setAdmin(true);
        obj.setBirthday(new Date(1999, Calendar.JUNE,4));

        responseService.outputResponse(resp, responseService.toJson(obj), 200);

        System.out.println("HOla soy el post y ya te envie el json");

    }

    private void outputResponse(HttpServletResponse response,String payload, int status){
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
}
