package ma.teleexpertise.repository;

import jakarta.persistence.*;
import ma.teleexpertise.entity.*;
import java.util.List;

public class ExpertiseRepository {
    private final EntityManager em;
    public ExpertiseRepository(EntityManager em) { this.em = em; }
    public ExpertiseRequest findById(Long id) { return em.find(ExpertiseRequest.class, id); }
    public ExpertiseRequest lock(Long id) { return em.find(ExpertiseRequest.class, id, LockModeType.PESSIMISTIC_WRITE); }
    public List<ExpertiseRequest> bySpecialist(Long id) { return em.createQuery("select r from ExpertiseRequest r where r.specialist.id = :id order by r.createdAt desc", ExpertiseRequest.class).setParameter("id", id).getResultList(); }
}
