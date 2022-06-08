package com.waifus.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.waifus.exceptions.MultimediaException;
import com.waifus.model.Multimedia;
import com.waifus.services.JWTService;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Properties;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class MultimediaCreationServlet extends HttpServlet {
    private Properties propHidden;
    private Properties prop;

    @Override
    public void init() throws ServletException {
        super.init();
        this.propHidden = PropertiesService.getProperties("hidden");
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.prop = PropertiesService.getProperties("config_es");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<Multimedia> responseService = new ResponseService<Multimedia>();
        String jwt = req.getHeader("Authorization");
        int idThread = Integer.parseInt(req.getHeader("idThread"));
        try {
            Multimedia multimedia = new Multimedia();
            JWTService.verifyJWT(jwt);
            Timestamp ts = Timestamp.from(Instant.now());
            String fileName = ts.toString().replace("-","").replace(".","").replace(":","").replace(" ","") + ".png";
            for (Part part : req.getParts()) {
                part.write(propHidden.getProperty("images.directory")+fileName);
            }
            multimedia.setThread(idThread);
            multimedia.setDirectory(fileName);
            multimedia.add();
        }catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }
    }
}
