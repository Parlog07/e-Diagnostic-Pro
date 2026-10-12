package ma.teleexpertise.repository;

import jakarta.persistence.*;
import ma.teleexpertise.entity.*;
import java.util.List;

public class PatientRepository {
    private final EntityManager em;
    public PatientRepository(EntityManager em) { this.em = em; }
    public Patient findById(Long id) { return em.find(Patient.class, id); }
    public Patient lock(Long id) { return em.find(Patient.class, id, LockModeType.PESSIMISTIC_WRITE); }
    public boolean numberExists(String number) {
        return em.createQuery("select count(p) from Patient p where p.socialSecurityNumber = :number", Long.class).setParameter("number", number).getSingleResult() > 0;
    }
    public List<Patient> search(String text) {
        return em.createQuery("select p from Patient p where lower(concat(p.firstName, ' ', p.lastName)) like :q or lower(p.socialSecurityNumber) like :q order by p.lastName", Patient.class).setParameter("q", "%" + text.toLowerCase() + "%").getResultList();
    }
    public List<Patient> arrivals() { return em.createQuery("select p from Patient p order by p.arrivalAt", Patient.class).getResultList(); }
    public List<Patient> waiting() { return em.createQuery("select p from Patient p where p.waiting = true order by p.arrivalAt", Patient.class).getResultList(); }
    public void save(Patient patient) { em.persist(patient); }
    public void saveVitals(VitalSigns signs) { em.persist(signs); }
    public boolean hasOpenConsultation(Long patientId) {
        return em.createQuery("select count(c) from Consultation c where c.patient.id = :id and c.status <> :done", Long.class).setParameter("id", patientId).setParameter("done", ma.teleexpertise.enums.ConsultationStatus.TERMINEE).getSingleResult() > 0;
    }
}
