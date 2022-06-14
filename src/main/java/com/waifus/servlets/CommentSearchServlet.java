package com.waifus.servlets;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.exceptions.CommentException;
import com.waifus.model.Comment;
import com.waifus.services.JWTService;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;


public class CommentSearchServlet extends HttpServlet {
    private Properties prop;

    @Override
    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<Comment> responseService = new ResponseService<Comment>();
        int idThread = Integer.parseInt(req.getParameter("idThread"));
        int idx = Integer.parseInt(req.getParameter("idx"));
        int pag = Integer.parseInt(req.getParameter("pag"));
        Comment comment = new Comment();
        ArrayList<Comment> comments= new ArrayList<Comment>();
        try {
            comments = comment.search(idx, pag, idThread);
            int count = comment.count(idThread);
            JsonObject json = new JsonObject();
            json.add("comments", new Gson().toJsonTree(comments));
            json.add("count", new JsonPrimitive(count));
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
        String jwt = req.getHeader("Authorization");
        ResponseService<Comment> responseService = new ResponseService<Comment>();
        Comment comment = new Gson().fromJson(req.getReader(), Comment.class);
        try{
            DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
            comment.setUser(Integer.parseInt(String.valueOf(decodedJWT.getClaim("idUser"))));
            comment.setThread(Integer.parseInt(req.getParameter("idThread")));
            comment=comment.add();
            JsonObject json = new JsonObject();
            json.add("added", new JsonPrimitive("ok"));
            json.add("comment", new Gson().toJsonTree(comment));
            responseService.outputResponse(resp, json.toString(), 200);
        }catch (CommentException e){
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
