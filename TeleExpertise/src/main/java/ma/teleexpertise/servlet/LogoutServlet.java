package ma.teleexpertise.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException { req.getSession().invalidate(); redirect(req, res, "/login"); }
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException { res.sendError(405); }
}
