package ma.teleexpertise.repository;

import jakarta.persistence.*;
import ma.teleexpertise.entity.*;
import java.util.List;

public class ConsultationRepository {
    private final EntityManager em;
    public ConsultationRepository(EntityManager em) { this.em = em; }
    public Consultation findById(Long id) { return em.find(Consultation.class, id); }
    public Consultation lock(Long id) { return em.find(Consultation.class, id, LockModeType.PESSIMISTIC_WRITE); }
    public List<Consultation> byDoctor(Long doctorId) { return em.createQuery("select c from Consultation c where c.doctor.id = :id order by c.createdAt desc", Consultation.class).setParameter("id", doctorId).getResultList(); }
    public void save(Consultation consultation) { em.persist(consultation); }
    public void saveAct(MedicalAct act) { em.persist(act); }
    public void saveRequest(ExpertiseRequest request) { em.persist(request); }
}
