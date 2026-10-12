package ma.teleexpertise.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import ma.teleexpertise.entity.*;
import ma.teleexpertise.enums.Priority;
import ma.teleexpertise.service.*;
import java.io.IOException;

@WebServlet("/doctor/consultations")
public class DoctorServlet extends BaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ConsultationService service = new ConsultationService(); Long doctorId = user(req).getId();
        try {
            String action = req.getParameter("action");
            if ("record".equals(action)) { req.setAttribute("patient", new PatientService().record(id(req, "patientId"), doctorId, false)); view(req, res, "doctor/record"); return; }
            if ("details".equals(action)) {
                Consultation c = service.details(id(req, "id"), doctorId); req.setAttribute("consultation", c); req.setAttribute("total", service.total(c)); req.setAttribute("specialists", new SpecialistService().search(req.getParameter("specialty"))); view(req, res, "doctor/details"); return;
            }
            req.setAttribute("waiting", new PatientService().waiting()); req.setAttribute("consultations", service.list(doctorId)); view(req, res, "doctor/list");
        } catch (RuntimeException e) { failure(req, res, e); if (!res.isCommitted()) redirect(req, res, "/doctor/consultations"); }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ConsultationService service = new ConsultationService(); Long doctorId = user(req).getId(); Long consultationId = null;
        try {
            String action = req.getParameter("action");
            if ("create".equals(action)) consultationId = service.create(id(req, "patientId"), doctorId, req.getParameter("reason"), req.getParameter("observations"));
            else {
                consultationId = id(req, "id");
                if ("update".equals(action) || "finish".equals(action)) service.update(consultationId, doctorId, req.getParameter("observations"), req.getParameter("diagnosis"), req.getParameter("treatment"), "finish".equals(action));
                else if ("act".equals(action)) service.addAct(consultationId, doctorId, req.getParameter("name"), decimal(req, "price", false));
                else if ("request".equals(action)) service.request(consultationId, doctorId, id(req, "specialistId"), req.getParameter("question"), Priority.valueOf(req.getParameter("priority")));
                else throw new IllegalArgumentException("Action invalide.");
            }
        } catch (RuntimeException e) { failure(req, res, e); }
        if (!res.isCommitted()) redirect(req, res, consultationId == null ? "/doctor/consultations" : "/doctor/consultations?action=details&id=" + consultationId);
    }
}
