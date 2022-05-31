package com.waifus.servlets;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.model.User;
import com.waifus.services.EmailService;
import com.waifus.services.JWTService;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class ActivationOTPServlet extends HttpServlet {

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
        User user = new User(Integer.parseInt(String.valueOf(req.getParameter("idUser"))));
        try {
            user = user.get();
            String codeSent = email.generateOTP();
            email.sendMail(email.activationOTPHtml(codeSent, user.getNickname(), user.getEmail()), user.getEmail(), email.activationOTPSubject(codeSent));
            JsonObject json = new JsonObject();
            String jwt = JWTService.createJWTOTP(user, codeSent);
            json.add("activationOTP",new JsonPrimitive(jwt));
            responseService.outputResponse(resp, json.toString(), 200);
        }catch (MessagingException e){
            System.out.println(prop.getProperty("error.email"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.email")), 400);
        }catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<User> responseService = new ResponseService<User>();
        String jwt = req.getHeader("Authorization");
        System.out.println("JWT:" + jwt);
        try {
            DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
            User user = new User (Integer.parseInt(String.valueOf(decodedJWT.getClaim("idUser"))));
            user = user.get();
            Claim codeSent = decodedJWT.getClaim("OTP");
            String codeRec = req.getParameter("codeRec");
            if(codeRec.equals(codeSent.asString())){
                user.setActivated(true);
                user.update();
                JsonObject json = new JsonObject();
                json.add("result", new JsonPrimitive("ok"));
                json.add("user", new Gson().toJsonTree(user));
                responseService.outputResponse(resp, json.toString(), 200);
            }else {
                JsonObject json = new JsonObject();
                json.add("result", new JsonPrimitive("ko"));
                json.add("user", new Gson().toJsonTree(user));
                responseService.outputResponse(resp, json.toString(), 202);
            }
        }catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }
    }
}
