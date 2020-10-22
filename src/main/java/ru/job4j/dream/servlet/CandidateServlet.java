package ru.job4j.dream.servlet;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.Store;

public class CandidateServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Store.instOf().save(new Candidate(
				0, 
				req.getParameter("firstname"), 
				req.getParameter("lastname"), 
				req.getParameter("position")
		));
		resp.sendRedirect(req.getContextPath() + "/candidates.jsp");
	}
}
