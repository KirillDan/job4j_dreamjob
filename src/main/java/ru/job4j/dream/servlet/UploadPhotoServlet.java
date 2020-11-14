package ru.job4j.dream.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ru.job4j.dream.store.PsqlStore;

public class UploadPhotoServlet extends HttpServlet {	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(req);
			PsqlStore.instOf().save(Integer.valueOf(req.getParameter("id")), items);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		resp.sendRedirect(req.getContextPath() + "/candidates.do");
	}
}
