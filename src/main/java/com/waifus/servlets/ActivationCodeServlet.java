package com.waifus.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.exceptions.UserException;
import com.waifus.model.User;
import com.waifus.services.EmailService;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;
import com.waifus.services.SecurityService;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class ActivationCodeServlet extends HttpServlet {

    private Properties prop;

    @Override
    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        ResponseService<User> responseService = new ResponseService<User>();
        EmailService email = new EmailService();
        try {
            int code = email.activationCode();
            email.sendMail(email.activationCodeHtml(code, req.getParameter("nickname"), req.getParameter("email")), req.getParameter("email"), email.activationCodeSubject(code));
            JsonObject json = new JsonObject();
            json.add("activationCode",new JsonPrimitive(code));
            responseService.outputResponse(resp, json.toString(), 200);
        }catch (MessagingException e){
            System.out.println(prop.getProperty("msg.error"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("msg.error")), 400);
        }catch (Exception e){
            System.out.println(prop.getProperty("resp.error"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("resp.error")), 400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
