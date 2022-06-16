package com.waifus.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.waifus.model.Channel;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class MostThreadsServlet extends HttpServlet {
    private Properties prop;

    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<Channel> responseService = new ResponseService<Channel>();
        Channel channelAux = new Channel();
        ArrayList<Channel> channelsAux = new ArrayList<Channel>();
        ArrayList<Channel> channels = new ArrayList<Channel>();
        try {
            channelsAux = channelAux.channelsWMostThreads();
            for (Channel channel: channelsAux) {
                channel = channel.get();
                channel.setThreads(channel.count());
                channels.add(channel);
            }
            JsonObject json = new JsonObject();
            json.add("channels", new Gson().toJsonTree(channels));
            responseService.outputResponse(resp, json.toString(), 200);
        }catch (SQLException e){
            System.out.println(prop.getProperty("error.db"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.db")), 400);
        }catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
