package com.waifus.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.model.Comment;
import com.waifus.model.Thread;
import com.waifus.model.User;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class DislikeCommentServlet extends HttpServlet {
    private Properties prop;

    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<User> responseService = new ResponseService<User>();
        Comment comment = new Comment(Integer.parseInt(req.getParameter("idComment")));
        try {
            comment = comment.get();
            int idUser = comment.getUser();
            User user = new User(idUser);
            user.dislikeCommment();
            user = user.get();
            JsonObject json = new JsonObject();
            json.add("dislike", new JsonPrimitive("ok"));
            json.add("user", new Gson().toJsonTree(user));
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
