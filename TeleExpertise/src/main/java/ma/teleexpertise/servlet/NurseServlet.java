package ma.teleexpertise.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import ma.teleexpertise.entity.*;
import ma.teleexpertise.service.PatientService;
import java.time.LocalDate;
import java.io.IOException;

@WebServlet("/nurse/patients")
public class NurseServlet extends BaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PatientService service = new PatientService();
        try {
            String action = req.getParameter("action");
            if ("new".equals(action)) { view(req, res, "nurse/new"); return; }
            if ("record".equals(action)) { req.setAttribute("patient", service.record(id(req, "id"), null, true)); view(req, res, "nurse/record"); return; }
            req.setAttribute("patients", "search".equals(action) ? service.search(req.getParameter("q")) : service.today());
            req.setAttribute("search", "search".equals(action)); view(req, res, "nurse/list");
        } catch (RuntimeException e) { failure(req, res, e); if (!res.isCommitted()) redirect(req, res, "/nurse/patients"); }
    }
    private VitalSigns vitals(HttpServletRequest req) {
        VitalSigns v = new VitalSigns(); v.setBloodPressure(req.getParameter("bloodPressure")); v.setHeartRate(number(req, "heartRate")); v.setTemperature(decimal(req, "temperature", false)); v.setRespiratoryRate(number(req, "respiratoryRate")); v.setWeight(decimal(req, "weight", true)); v.setHeight(decimal(req, "height", true)); return v;
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PatientService service = new PatientService();
        String action = req.getParameter("action");
        try {
            if ("register".equals(action)) {
                Patient p = new Patient(); p.setFirstName(req.getParameter("firstName")); p.setLastName(req.getParameter("lastName"));
                try { p.setBirthDate(LocalDate.parse(req.getParameter("birthDate"))); } catch (RuntimeException e) { throw new IllegalArgumentException("Date de naissance invalide."); }
                p.setSocialSecurityNumber(req.getParameter("socialSecurityNumber")); p.setPhone(req.getParameter("phone")); p.setAddress(req.getParameter("address")); p.setInsurance(req.getParameter("insurance")); p.setHistory(req.getParameter("history")); p.setAllergies(req.getParameter("allergies")); p.setCurrentTreatment(req.getParameter("currentTreatment"));
                Long id = service.register(p, vitals(req), user(req).getId()); redirect(req, res, "/nurse/patients?action=record&id=" + id); return;
            }
            if ("receive".equals(action)) { service.receive(id(req, "id"), vitals(req), user(req).getId()); redirect(req, res, "/nurse/patients?action=record&id=" + id(req, "id")); return; }
            throw new IllegalArgumentException("Action invalide.");
        } catch (RuntimeException e) {
            failure(req, res, e);
            if (!res.isCommitted()) {
                if ("register".equals(action)) view(req, res, "nurse/new");
                else redirect(req, res, "/nurse/patients");
            }
        }
    }
}
