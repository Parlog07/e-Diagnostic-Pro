package ma.teleexpertise.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import ma.teleexpertise.util.*;
import java.io.IOException;
import java.math.BigDecimal;

public abstract class BaseServlet extends HttpServlet {
    protected SessionUser user(HttpServletRequest req) { return (SessionUser) req.getSession().getAttribute("user"); }
    protected Long id(HttpServletRequest req, String name) {
        try { Long id = Long.valueOf(req.getParameter(name)); if (id < 1) throw new NumberFormatException(); return id; }
        catch (RuntimeException e) { throw new IllegalArgumentException("Identifiant invalide."); }
    }
    protected BigDecimal decimal(HttpServletRequest req, String name, boolean optional) {
        String value = req.getParameter(name);
        if (optional && (value == null || value.isBlank())) return null;
        try { return new BigDecimal(value); } catch (RuntimeException e) { throw new IllegalArgumentException("Valeur numérique invalide : " + name); }
    }
    protected int number(HttpServletRequest req, String name) {
        try { return Integer.parseInt(req.getParameter(name)); } catch (RuntimeException e) { throw new IllegalArgumentException("Valeur numérique invalide : " + name); }
    }
    protected void view(HttpServletRequest req, HttpServletResponse res, String name) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + name + ".jsp").forward(req, res);
    }
    protected void redirect(HttpServletRequest req, HttpServletResponse res, String path) throws IOException { res.sendRedirect(req.getContextPath() + path); }
    protected void failure(HttpServletRequest req, HttpServletResponse res, RuntimeException e) throws IOException {
        if (e instanceof AccessDeniedException) { res.sendError(403); return; }
        if (e instanceof IllegalArgumentException) { req.getSession().setAttribute("error", e.getMessage()); return; }
        req.getSession().setAttribute("error", "Opération impossible. Vérifiez les données ou la connexion à la base.");
    }
}
