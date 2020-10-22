package ru.job4j.dream.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.Store;

import java.io.IOException;
import java.time.LocalDate;

public class PostServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Store.instOf().save(
				new Post(0, req.getParameter("name"), req.getParameter("description"), LocalDate.now().toString()));
		resp.sendRedirect(req.getContextPath() + "/posts.jsp");
	}
}