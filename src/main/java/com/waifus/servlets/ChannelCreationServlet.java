package com.waifus.servlets;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.exceptions.ChannelException;
import com.waifus.model.Channel;
import com.waifus.services.JWTService;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Properties;

public class ChannelCreationServlet extends HttpServlet {
    private Properties prop;

    @Override
    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<Channel> responseService = new ResponseService<Channel>();
        Channel channel = new Channel();
        channel.setName(req.getParameter("name"));
        try{
            boolean name = channel.nameCheck();
            JsonObject json = new JsonObject();
            json.add("name", new JsonPrimitive(name));
            responseService.outputResponse(resp, json.toString(), 200);
        }catch (SQLException e){
            System.out.println(prop.getProperty("error.db"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.db")), 400);
        }catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(e.getMessage()), 400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jwt = req.getHeader("Authorization");
        ResponseService<Channel> responseService = new ResponseService<Channel>();
        Channel channel = new Gson().fromJson(req.getReader(), Channel.class);
        try{
            DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
            JsonObject json = new JsonObject();
            json.add("added", new JsonPrimitive("ko"));
            channel.setUser(Integer.parseInt(String.valueOf(decodedJWT.getClaim("idUser"))));
            if (Boolean.parseBoolean(String.valueOf(decodedJWT.getClaim("admin"))) || Integer.parseInt(String.valueOf(decodedJWT.getClaim("karma"))) >= 1000){
                channel=channel.add();
                json.add("added", new JsonPrimitive("ok"));
                json.add("channel", new Gson().toJsonTree(channel));
                responseService.outputResponse(resp, json.toString(), 200);
            }else{
                json.add("added", new JsonPrimitive("ko"));
                json.add("error", new JsonPrimitive(prop.getProperty("error.noPermit")));
                responseService.outputResponse(resp, json.toString(), 401);
            }
        }catch (ChannelException e){
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(e.getMessage()), 400);
        }catch (SQLException e){
            System.out.println(prop.getProperty("error.db"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.db")), 400);
        }catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(e.getMessage()), 400);
        }
    }
}
