package com.waifus.servlets;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.waifus.model.Channel;
import com.waifus.model.User;
import com.waifus.services.JWTService;
import com.waifus.services.PropertiesService;
import com.waifus.services.ResponseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Properties;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class PhotoProfileServlet extends HttpServlet {

    private Properties propHidden;
    private Properties prop;

    @Override
    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.propHidden = PropertiesService.getProperties("hidden");
        this.prop = PropertiesService.getProperties("config_es");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseService<Channel> responseService = new ResponseService<Channel>();
        String jwt = req.getHeader("Authorization");

        try{
            DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
            int idUser = Integer.parseInt(String.valueOf(decodedJWT.getClaim("idUser")));
            User user = new User();
            user.setIdUser(idUser);
            user = user.get();
            Timestamp ts = Timestamp.from(Instant.now());
            String fileName = ts.toString().replace("-","").replace(".","").replace(":","").replace(" ","") + ".png";
            for (Part part : req.getParts()) {
                part.write(propHidden.getProperty("images.directory")+fileName);
            }
            user.setProfilePhoto(fileName);
            user.update();
        }catch (Exception e){
            System.out.println(prop.getProperty("error.generic"));
            System.out.println(e.getMessage());
            responseService.outputResponse(resp, responseService.errorResponse(prop.getProperty("error.generic")), 400);
        }

    }

}
