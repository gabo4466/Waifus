package com.waifus.servlets;

import com.waifus.services.PropertiesService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Properties;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class MainPhotoChannelServlet extends HttpServlet {

    private Properties propHidden;

    @Override
    public void init() throws ServletException {
        super.init();
        // AQUI SE HARIA EL CAMBIO DE IDIOMA
        this.propHidden = PropertiesService.getProperties("hidden");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("photo");
        Timestamp ts = Timestamp.from(Instant.now());
        String fileName = ts.toString().replace("-","").replace(".","").replace(":","").replace(" ","") + ".png";
        System.out.println(fileName);
        System.out.println("HOLA");
        for (Part part : request.getParts()) {
            part.write(propHidden.getProperty("images.directory")+fileName);
        }
    }

}
