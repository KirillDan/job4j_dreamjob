package ru.job4j.dream.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.dream.store.PsqlStore;

public class DeletingCandidateServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer id = Integer.valueOf(req.getParameter("id"));
		PsqlStore.instOf().deleteCandidate(id);
		resp.sendRedirect(req.getContextPath() + "/candidates.do");
	}

}
