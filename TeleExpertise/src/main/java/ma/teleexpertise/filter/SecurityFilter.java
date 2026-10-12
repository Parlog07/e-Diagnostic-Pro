package ma.teleexpertise.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import ma.teleexpertise.util.*;

public class SecurityFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request; HttpServletResponse res = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8"); res.setCharacterEncoding("UTF-8");
        res.setHeader("X-Content-Type-Options", "nosniff"); res.setHeader("X-Frame-Options", "DENY");
        res.setHeader("Referrer-Policy", "same-origin");
        res.setHeader("Content-Security-Policy", "default-src 'self'; style-src 'self'; script-src 'self'; img-src 'self' data:; base-uri 'self'; form-action 'self'; frame-ancestors 'none'");
        String path = req.getServletPath();
        if (path.startsWith("/assets/")) { chain.doFilter(req, res); return; }
        res.setHeader("Cache-Control", "no-store");
        HttpSession session = req.getSession(false);
        SessionUser user = session == null ? null : (SessionUser) session.getAttribute("user");
        if (!path.equals("/login") && user == null) { res.sendRedirect(req.getContextPath() + "/login"); return; }
        if (user != null && ((path.startsWith("/nurse/") && !user.getRole().equals("NURSE")) || (path.startsWith("/doctor/") && !user.getRole().equals("DOCTOR")) || (path.startsWith("/specialist/") && !user.getRole().equals("SPECIALIST")))) {
            res.sendError(403); return;
        }
        if (req.getMethod().equals("POST") && !Csrf.valid(session, req.getParameter("csrf"))) { res.sendError(403); return; }
        chain.doFilter(req, res);
    }
}
