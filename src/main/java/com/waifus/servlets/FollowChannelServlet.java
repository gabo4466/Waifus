package com.waifus.servlets;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.exceptions.FollowChannelException;
import com.waifus.exceptions.UserException;
import com.waifus.model.FollowChannel;
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

public class FollowChannelServlet extends HttpServlet {
    private Properties prop;

    @Override
    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<FollowChannel> responseService = new ResponseService<FollowChannel>();
        int idChannel = Integer.parseInt(req.getParameter("idChannel"));
        String jwt = req.getHeader("Authorization");
        JsonObject json = new JsonObject();
        try {
            DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
            FollowChannel followChannel = new FollowChannel(idChannel, Integer.parseInt(String.valueOf(decodedJWT.getClaim("idUser"))));
            followChannel.follows();
            json.add("follow",new JsonPrimitive(true));
            responseService.outputResponse(resp,json.toString(),200);
        }catch (FollowChannelException ex) {
            json.add("follow",new JsonPrimitive(false));
            responseService.outputResponse(resp,json.toString(),200);
        } catch (Exception ex) {
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<FollowChannel> responseService = new ResponseService<FollowChannel>();
        String jwt = req.getHeader("Authorization");
        int idChannel = Integer.parseInt(req.getParameter("idChannel"));
        String dateFollow = req.getParameter("dateFollow");
        JsonObject json = new JsonObject();
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWTService.verifyJWT(jwt);
            FollowChannel followChannel = new FollowChannel(idChannel, Integer.parseInt(String.valueOf(decodedJWT.getClaim("idUser"))),dateFollow);
            followChannel.add();
            json.add("follow",new JsonPrimitive(true));
            responseService.outputResponse(resp,json.toString(),200);
        }catch (FollowChannelException e){
            FollowChannel followChannel = new FollowChannel(idChannel, Integer.parseInt(String.valueOf(decodedJWT.getClaim("idUser"))),dateFollow);
            try {
                followChannel.delete();
                json.add("follow",new JsonPrimitive(false));
                responseService.outputResponse(resp,json.toString(),200);
            } catch (Exception ex) {
                responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
            }

        }catch (SQLException e){
            System.out.println(prop.getProperty("error.db"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }catch (JWTVerificationException e){
            responseService.notLoggedResponse(resp);
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }

    }
}