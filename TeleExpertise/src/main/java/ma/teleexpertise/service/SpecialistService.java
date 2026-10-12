package ma.teleexpertise.service;

import jakarta.persistence.*;
import ma.teleexpertise.entity.*;
import ma.teleexpertise.enums.*;
import ma.teleexpertise.repository.*;
import ma.teleexpertise.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class SpecialistService {
    private final EntityManagerFactory factory;
    public SpecialistService() { this(JpaUtil.getFactory()); }
    public SpecialistService(EntityManagerFactory factory) { this.factory = factory; }
    public List<Specialist> filter(List<Specialist> specialists, String specialty) {
        String text = Validation.optional(specialty, "Spécialité", 100).toLowerCase(Locale.ROOT);
        return specialists.stream().filter(s -> s.getSpecialty().toLowerCase(Locale.ROOT).contains(text)).sorted(Comparator.comparing(Specialist::getTariff)).toList();
    }
    public List<Specialist> search(String specialty) {
        try (EntityManager em = factory.createEntityManager()) { return filter(new UserRepository(em).specialists(), specialty); }
    }
    public Specialist profile(Long id) {
        try (EntityManager em = factory.createEntityManager()) {
            Specialist s = new UserRepository(em).findById(Specialist.class, id);
            if (s == null) throw new AccessDeniedException(); return s;
        }
    }
    public void configure(Long id, String specialty, BigDecimal tariff) {
        specialty = Validation.required(specialty, "Spécialité", 100); tariff = Validation.money(tariff);
        try (EntityManager em = factory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction(); tx.begin();
            try {
                Specialist s = em.find(Specialist.class, id, LockModeType.PESSIMISTIC_WRITE);
                if (s == null) throw new AccessDeniedException(); s.setSpecialty(specialty); s.setTariff(tariff); tx.commit();
            } catch (RuntimeException e) { if (tx.isActive()) tx.rollback(); throw e; }
        }
    }
    public List<ExpertiseRequest> list(Long specialistId, ExpertiseStatus status, Priority priority) {
        try (EntityManager em = factory.createEntityManager()) {
            return new ExpertiseRepository(em).bySpecialist(specialistId).stream().filter(r -> status == null || r.getStatus() == status).filter(r -> priority == null || r.getPriority() == priority).toList();
        }
    }
    public ExpertiseRequest details(Long id, Long specialistId) {
        try (EntityManager em = factory.createEntityManager()) {
            ExpertiseRequest r = new ExpertiseRepository(em).findById(id);
            if (r == null) throw new IllegalArgumentException("Demande introuvable.");
            if (!r.getSpecialist().getId().equals(specialistId)) throw new AccessDeniedException();
            r.getConsultation().getMedicalActs().size(); r.getConsultation().getPatient().getVitalSigns().size();
            return r;
        }
    }
    public void applyResponse(ExpertiseRequest r, Long specialistId, String opinion, String recommendations) {
        if (!r.getSpecialist().getId().equals(specialistId)) throw new AccessDeniedException();
        if (r.getStatus() == ExpertiseStatus.TERMINEE) throw new IllegalArgumentException("Cette demande est déjà terminée.");
        if (r.getConsultation().getStatus() != ConsultationStatus.EN_ATTENTE_AVIS_SPECIALISTE) throw new IllegalArgumentException("Consultation indisponible.");
        opinion = Validation.required(opinion, "Avis", 3000);
        recommendations = Validation.required(recommendations, "Recommandations", 3000);
        r.setOpinion(opinion); r.setRecommendations(recommendations); r.setStatus(ExpertiseStatus.TERMINEE); r.setCompletedAt(LocalDateTime.now());
        r.getConsultation().setStatus(ConsultationStatus.AVIS_RECU);
    }
    public void respond(Long id, Long specialistId, String opinion, String recommendations) {
        try (EntityManager em = factory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction(); tx.begin();
            try {
                ExpertiseRequest r = new ExpertiseRepository(em).lock(id);
                if (r == null) throw new IllegalArgumentException("Demande introuvable.");
                em.lock(r.getConsultation(), LockModeType.PESSIMISTIC_WRITE);
                applyResponse(r, specialistId, opinion, recommendations); tx.commit();
            } catch (RuntimeException e) { if (tx.isActive()) tx.rollback(); throw e; }
        }
    }
}
