package ru.job4j.dream.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;

public class CandidateServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
		req.setAttribute("user", req.getSession().getAttribute("user"));
		req.getRequestDispatcher("candidates.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		PsqlStore.instOf().save(new Candidate(Integer.valueOf(req.getParameter("id")), req.getParameter("firstname"),
				req.getParameter("lastname"), req.getParameter("position")));
		resp.sendRedirect(req.getContextPath() + "/candidates.do");
	}
}
