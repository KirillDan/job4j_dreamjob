package ru.job4j.dream.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PsqlStore;

import java.io.IOException;
import java.time.LocalDate;

public class PostServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("posts", PsqlStore.instOf().findAllPosts());
		req.getRequestDispatcher("posts.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		PsqlStore.instOf().save(new Post(Integer.valueOf(req.getParameter("id")), req.getParameter("name"),
				req.getParameter("description"), LocalDate.now().toString()));
		resp.sendRedirect(req.getContextPath() + "/posts.do");
	}
}