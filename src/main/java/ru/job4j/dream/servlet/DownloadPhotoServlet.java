package ru.job4j.dream.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DownloadPhotoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int photoId = Integer.valueOf(req.getParameter("photoId"));
        resp.setContentType("photoId=" + photoId);
        resp.setContentType("image/png");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + photoId + "\"");
        File file = new File("bin" + File.separator + "images" + File.separator + "phote_id" + File.separator + photoId);
        try (FileInputStream in = new FileInputStream(file)) {
            resp.getOutputStream().write(in.readAllBytes());
        }
    }
}
