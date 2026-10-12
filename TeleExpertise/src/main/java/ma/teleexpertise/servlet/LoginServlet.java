package ma.teleexpertise.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import ma.teleexpertise.entity.User;
import ma.teleexpertise.service.AuthService;
import ma.teleexpertise.util.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") != null) { redirect(req, res, user(req).getHome()); return; }
        if (session.getAttribute("csrf") == null) Csrf.create(session);
        view(req, res, "auth/login");
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User authenticated = new AuthService().login(req.getParameter("email"), req.getParameter("password"));
        if (authenticated == null) { req.setAttribute("error", "Adresse e-mail ou mot de passe incorrect."); view(req, res, "auth/login"); return; }
        req.getSession().invalidate(); HttpSession session = req.getSession(true); Csrf.create(session);
        SessionUser user = new SessionUser(authenticated); session.setAttribute("user", user);
        redirect(req, res, user.getHome());
    }
}
