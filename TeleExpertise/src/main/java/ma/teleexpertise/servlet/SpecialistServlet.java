package ma.teleexpertise.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import ma.teleexpertise.enums.*;
import ma.teleexpertise.entity.ExpertiseRequest;
import ma.teleexpertise.service.*;
import java.io.IOException;

@WebServlet("/specialist/requests")
public class SpecialistServlet extends BaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        SpecialistService service = new SpecialistService(); Long specialistId = user(req).getId();
        try {
            String action = req.getParameter("action");
            if ("profile".equals(action)) { req.setAttribute("profile", service.profile(specialistId)); view(req, res, "specialist/profile"); return; }
            if ("details".equals(action)) {
                ExpertiseRequest r = service.details(id(req, "id"), specialistId); req.setAttribute("expertise", r); req.setAttribute("consultation", r.getConsultation()); req.setAttribute("total", new ConsultationService().total(r.getConsultation())); view(req, res, "specialist/details"); return;
            }
            String status = req.getParameter("status"), priority = req.getParameter("priority");
            req.setAttribute("requests", service.list(specialistId, status == null || status.isBlank() ? null : ExpertiseStatus.valueOf(status), priority == null || priority.isBlank() ? null : Priority.valueOf(priority)));
            view(req, res, "specialist/list");
        } catch (RuntimeException e) { failure(req, res, e); if (!res.isCommitted()) redirect(req, res, "/specialist/requests"); }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        SpecialistService service = new SpecialistService(); String action = req.getParameter("action");
        try {
            if ("profile".equals(action)) service.configure(user(req).getId(), req.getParameter("specialty"), decimal(req, "tariff", false));
            else if ("respond".equals(action)) service.respond(id(req, "id"), user(req).getId(), req.getParameter("opinion"), req.getParameter("recommendations"));
            else throw new IllegalArgumentException("Action invalide.");
        } catch (RuntimeException e) { failure(req, res, e); }
        if (!res.isCommitted()) redirect(req, res, "profile".equals(action) ? "/specialist/requests?action=profile" : "/specialist/requests");
    }
}
