package com.waifus.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.model.Thread;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class ThreadSearchServlet extends HttpServlet {
    private Properties prop;

    @Override
    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<Thread> responseService = new ResponseService<Thread>();
        Thread thread = new Thread();
        ArrayList<Thread> threads= new ArrayList<Thread>();
        int idx = Integer.parseInt(req.getParameter("idx"));
        int pag = Integer.parseInt(req.getParameter("pag"));
        String term = req.getParameter("term");
        int idChannel = Integer.parseInt(req.getParameter("idChannel"));
        try {
            thread.setChannel(idChannel);
            threads = thread.search(idx, pag, term);
            int count = thread.count(term);
            JsonObject json = new JsonObject();
            json.add("threads", new Gson().toJsonTree(threads));
            json.add("count", new JsonPrimitive(count));
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

    }
}
