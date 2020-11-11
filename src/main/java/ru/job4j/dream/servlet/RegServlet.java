package ru.job4j.dream.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

public class RegServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("reg.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession sc = req.getSession();
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		Store store = PsqlStore.instOf();
		if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
			req.setAttribute("error", "Пустой email или пароль");
			req.getRequestDispatcher("reg.jsp").forward(req, resp);
		} else {
			User user = new User(0, name, email, password);
			User findUser = store.findByEmail(email);
			if (findUser != null && findUser.getPassword().equals(password)) {
				req.setAttribute("error", "Повторяющийся email и пароль");
				req.getRequestDispatcher("reg.jsp").forward(req, resp);
			} else {
				store.save(user);
				sc.setAttribute("user", user);
				resp.sendRedirect(req.getContextPath() + "/index.do");
			}
		}

	}	
}
