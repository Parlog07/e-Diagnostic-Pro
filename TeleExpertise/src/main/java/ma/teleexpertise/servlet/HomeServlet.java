package ma.teleexpertise.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("")
public class HomeServlet extends BaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException { redirect(req, res, user(req).getHome()); }
}
