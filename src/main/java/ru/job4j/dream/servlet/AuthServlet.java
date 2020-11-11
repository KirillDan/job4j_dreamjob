package ru.job4j.dream.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import java.io.IOException;

public class AuthServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		if (!email.isEmpty() && !password.isEmpty()) {
			HttpSession sc = req.getSession();
			User admin = PsqlStore.instOf().findByEmail(email);
			if (admin == null) {
				req.setAttribute("error", "Не верный email");
				req.getRequestDispatcher("login.jsp").forward(req, resp);
			} else if (!admin.getPassword().equals(password)) {
				req.setAttribute("error", "Не верный пароль");
				req.getRequestDispatcher("login.jsp").forward(req, resp);
			} else {
				sc.setAttribute("user", admin);
				resp.sendRedirect(req.getContextPath() + "/posts.do");
			}
		} else {
			req.setAttribute("error", "Не верный email или пароль");
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		}
	}
}
