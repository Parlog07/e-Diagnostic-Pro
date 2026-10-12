package ma.teleexpertise.service;

import jakarta.persistence.*;
import ma.teleexpertise.entity.*;
import ma.teleexpertise.repository.*;
import ma.teleexpertise.util.*;
import java.time.*;
import java.util.*;

public class PatientService {
    private final EntityManagerFactory factory;
    public PatientService() { this(JpaUtil.getFactory()); }
    public PatientService(EntityManagerFactory factory) { this.factory = factory; }
    public void validate(Patient p) {
        p.setFirstName(Validation.required(p.getFirstName(), "Prénom", 100));
        p.setLastName(Validation.required(p.getLastName(), "Nom", 100));
        p.setSocialSecurityNumber(Validation.required(p.getSocialSecurityNumber(), "Numéro de sécurité sociale", 50));
        if (p.getBirthDate() == null || p.getBirthDate().isAfter(LocalDate.now()) || p.getBirthDate().isBefore(LocalDate.of(1900, 1, 1)))
            throw new IllegalArgumentException("Date de naissance invalide.");
        p.setPhone(Validation.optional(p.getPhone(), "Téléphone", 30));
        p.setAddress(Validation.optional(p.getAddress(), "Adresse", 300));
        p.setInsurance(Validation.optional(p.getInsurance(), "Mutuelle", 100));
        p.setHistory(Validation.optional(p.getHistory(), "Antécédents", 3000));
        p.setAllergies(Validation.optional(p.getAllergies(), "Allergies", 1000));
        p.setCurrentTreatment(Validation.optional(p.getCurrentTreatment(), "Traitement", 3000));
    }
    public void validateVitals(VitalSigns v) {
        v.setBloodPressure(Validation.required(v.getBloodPressure(), "Tension", 20));
        if (!v.getBloodPressure().matches("[0-9]{2,3}/[0-9]{2,3}")) throw new IllegalArgumentException("Tension : format attendu 120/80.");
        if (v.getHeartRate() < 1 || v.getHeartRate() > 300 || v.getRespiratoryRate() < 1 || v.getRespiratoryRate() > 100 || v.getTemperature() == null || v.getTemperature().doubleValue() < 25 || v.getTemperature().doubleValue() > 45 || v.getTemperature().scale() > 1)
            throw new IllegalArgumentException("Signes vitaux invalides. Vérifiez les valeurs et la température (une décimale).");
        for (java.math.BigDecimal value : Arrays.asList(v.getWeight(), v.getHeight())) {
            if (value != null && (value.signum() <= 0 || value.doubleValue() > 500 || value.scale() > 2)) throw new IllegalArgumentException("Poids ou taille invalide.");
        }
    }
    public List<Patient> search(String query) {
        try (EntityManager em = factory.createEntityManager()) {
            return new PatientRepository(em).search(Validation.optional(query, "Recherche", 100));
        }
    }
    public List<Patient> today() {
        try (EntityManager em = factory.createEntityManager()) {
            List<Patient> patients = new PatientRepository(em).arrivals().stream().filter(p -> p.getArrivalAt().toLocalDate().equals(LocalDate.now())).toList();
            for (Patient p : patients) p.getVitalSigns().size();
            return patients;
        }
    }
    public List<Patient> waiting() {
        try (EntityManager em = factory.createEntityManager()) { return new PatientRepository(em).waiting(); }
    }
    public Patient record(Long id, Long doctorId, boolean nurse) {
        try (EntityManager em = factory.createEntityManager()) {
            Patient p = new PatientRepository(em).findById(id);
            if (p == null) throw new IllegalArgumentException("Patient introuvable.");
            if (!nurse && !p.isWaiting() && p.getConsultations().stream().noneMatch(c -> c.getDoctor().getId().equals(doctorId))) throw new AccessDeniedException();
            p.getVitalSigns().size();
            p.getConsultations().size();
            if (!nurse) p.setConsultations(p.getConsultations().stream().filter(c -> c.getDoctor().getId().equals(doctorId)).toList());
            return p;
        }
    }
    public Long register(Patient patient, VitalSigns vitals, Long nurseId) {
        validate(patient); validateVitals(vitals);
        try (EntityManager em = factory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction(); tx.begin();
            try {
                PatientRepository repo = new PatientRepository(em);
                if (repo.numberExists(patient.getSocialSecurityNumber())) throw new IllegalArgumentException("Ce numéro de sécurité sociale existe déjà.");
                Nurse nurse = new UserRepository(em).findById(Nurse.class, nurseId);
                if (nurse == null) throw new AccessDeniedException();
                patient.setWaiting(true); patient.setArrivalAt(LocalDateTime.now()); repo.save(patient);
                vitals.setPatient(patient); vitals.setNurse(nurse); vitals.setRecordedAt(LocalDateTime.now()); repo.saveVitals(vitals);
                tx.commit(); return patient.getId();
            } catch (RuntimeException e) { if (tx.isActive()) tx.rollback(); throw e; }
        }
    }
    public void receive(Long patientId, VitalSigns vitals, Long nurseId) {
        validateVitals(vitals);
        try (EntityManager em = factory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction(); tx.begin();
            try {
                PatientRepository repo = new PatientRepository(em); Patient p = repo.lock(patientId);
                if (p == null) throw new IllegalArgumentException("Patient introuvable.");
                if (p.isWaiting() || repo.hasOpenConsultation(patientId)) throw new IllegalArgumentException("Ce patient est déjà en attente ou en consultation.");
                Nurse nurse = new UserRepository(em).findById(Nurse.class, nurseId);
                if (nurse == null) throw new AccessDeniedException();
                p.setWaiting(true); p.setArrivalAt(LocalDateTime.now());
                vitals.setPatient(p); vitals.setNurse(nurse); vitals.setRecordedAt(LocalDateTime.now()); repo.saveVitals(vitals);
                tx.commit();
            } catch (RuntimeException e) { if (tx.isActive()) tx.rollback(); throw e; }
        }
    }
}
