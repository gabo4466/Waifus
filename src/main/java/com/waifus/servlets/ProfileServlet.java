package com.waifus.servlets;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.waifus.model.User;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;
import com.waifus.services.JWTService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class ProfileServlet extends HttpServlet {
    private Properties prop;

    @Override
    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<User> responseService = new ResponseService<User>();
        String jwt = req.getHeader("Authorization");
        try{
            DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
            User user = new User (Integer.parseInt(String.valueOf(decodedJWT.getClaim("idUser"))));
            user = user.get();
            String jsonUser = responseService.toJson(user);
            responseService.outputResponse(resp, jsonUser,200);
        }catch (SQLException e){
            System.out.println(prop.getProperty("error.db"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }catch (JWTVerificationException e){
            responseService.notLoggedResponse(resp);
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }
}
