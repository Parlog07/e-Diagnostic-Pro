package ma.teleexpertise.service;

import jakarta.persistence.*;
import ma.teleexpertise.entity.*;
import ma.teleexpertise.enums.*;
import ma.teleexpertise.repository.*;
import ma.teleexpertise.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ConsultationService {
    private final EntityManagerFactory factory;
    public ConsultationService() { this(JpaUtil.getFactory()); }
    public ConsultationService(EntityManagerFactory factory) { this.factory = factory; }
    public BigDecimal total(Consultation c) {
        BigDecimal acts = c.getMedicalActs().stream().map(MedicalAct::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal fee = c.getExpertiseRequest() == null ? BigDecimal.ZERO : c.getExpertiseRequest().getFee();
        return c.getBaseCost().add(acts).add(fee);
    }
    public List<Consultation> list(Long doctorId) {
        try (EntityManager em = factory.createEntityManager()) { return new ConsultationRepository(em).byDoctor(doctorId); }
    }
    private void owner(Consultation c, Long doctorId) {
        if (c == null) throw new IllegalArgumentException("Consultation introuvable.");
        if (!c.getDoctor().getId().equals(doctorId)) throw new AccessDeniedException();
    }
    private void editable(Consultation c) {
        if (c.getStatus() != ConsultationStatus.EN_COURS && c.getStatus() != ConsultationStatus.AVIS_RECU)
            throw new IllegalArgumentException("Cette consultation ne peut pas être modifiée dans son état actuel.");
    }
    public Consultation details(Long id, Long doctorId) {
        try (EntityManager em = factory.createEntityManager()) {
            Consultation c = new ConsultationRepository(em).findById(id); owner(c, doctorId);
            c.getMedicalActs().size(); c.getPatient().getVitalSigns().size();
            return c;
        }
    }
    public Long create(Long patientId, Long doctorId, String reason, String observations) {
        reason = Validation.required(reason, "Motif", 1000);
        observations = Validation.optional(observations, "Observations", 3000);
        try (EntityManager em = factory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction(); tx.begin();
            try {
                PatientRepository patients = new PatientRepository(em); Patient p = patients.lock(patientId);
                if (p == null || !p.isWaiting() || patients.hasOpenConsultation(patientId)) throw new IllegalArgumentException("Patient indisponible dans la file d'attente.");
                GeneralPractitioner doctor = new UserRepository(em).findById(GeneralPractitioner.class, doctorId);
                if (doctor == null) throw new AccessDeniedException();
                Consultation c = new Consultation(); c.setPatient(p); c.setDoctor(doctor); c.setReason(reason); c.setObservations(observations); c.setCreatedAt(LocalDateTime.now());
                new ConsultationRepository(em).save(c); p.setWaiting(false);
                tx.commit(); return c.getId();
            } catch (RuntimeException e) { if (tx.isActive()) tx.rollback(); throw e; }
        }
    }
    public void update(Long id, Long doctorId, String observations, String diagnosis, String treatment, boolean finish) {
        observations = Validation.optional(observations, "Observations", 3000);
        diagnosis = finish ? Validation.required(diagnosis, "Diagnostic", 3000) : Validation.optional(diagnosis, "Diagnostic", 3000);
        treatment = finish ? Validation.required(treatment, "Traitement", 3000) : Validation.optional(treatment, "Traitement", 3000);
        try (EntityManager em = factory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction(); tx.begin();
            try {
                Consultation c = new ConsultationRepository(em).lock(id); owner(c, doctorId); editable(c);
                c.setObservations(observations); c.setDiagnosis(diagnosis); c.setTreatment(treatment);
                if (finish) c.setStatus(ConsultationStatus.TERMINEE);
                tx.commit();
            } catch (RuntimeException e) { if (tx.isActive()) tx.rollback(); throw e; }
        }
    }
    public void addAct(Long id, Long doctorId, String name, BigDecimal price) {
        name = Validation.required(name, "Acte", 200); price = Validation.money(price);
        try (EntityManager em = factory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction(); tx.begin();
            try {
                ConsultationRepository repo = new ConsultationRepository(em); Consultation c = repo.lock(id); owner(c, doctorId); editable(c);
                MedicalAct a = new MedicalAct(); a.setConsultation(c); a.setName(name); a.setPrice(price); repo.saveAct(a);
                tx.commit();
            } catch (RuntimeException e) { if (tx.isActive()) tx.rollback(); throw e; }
        }
    }
    public void request(Long id, Long doctorId, Long specialistId, String question, Priority priority) {
        question = Validation.required(question, "Question", 3000);
        if (priority == null) throw new IllegalArgumentException("Priorité obligatoire.");
        try (EntityManager em = factory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction(); tx.begin();
            try {
                ConsultationRepository repo = new ConsultationRepository(em); Consultation c = repo.lock(id); owner(c, doctorId); editable(c);
                if (c.getExpertiseRequest() != null) throw new IllegalArgumentException("Une expertise existe déjà pour cette consultation.");
                Specialist specialist = new UserRepository(em).findById(Specialist.class, specialistId);
                if (specialist == null) throw new IllegalArgumentException("Spécialiste introuvable.");
                ExpertiseRequest r = new ExpertiseRequest(); r.setConsultation(c); r.setSpecialist(specialist); r.setQuestion(question); r.setPriority(priority); r.setFee(specialist.getTariff()); r.setCreatedAt(LocalDateTime.now());
                repo.saveRequest(r); c.setExpertiseRequest(r); c.setStatus(ConsultationStatus.EN_ATTENTE_AVIS_SPECIALISTE);
                tx.commit();
            } catch (RuntimeException e) { if (tx.isActive()) tx.rollback(); throw e; }
        }
    }
}
