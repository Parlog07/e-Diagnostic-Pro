package ma.teleexpertise.repository;

import jakarta.persistence.*;
import ma.teleexpertise.entity.*;
import java.util.List;

public class UserRepository {
    private final EntityManager em;
    public UserRepository(EntityManager em) { this.em = em; }
    public User findByEmail(String email) {
        return em.createQuery("select u from User u where lower(u.email) = :email", User.class).setParameter("email", email.toLowerCase()).getResultStream().findFirst().orElse(null);
    }
    public <T extends User> T findById(Class<T> type, Long id) { return em.find(type, id); }
    public List<Specialist> specialists() { return em.createQuery("select s from Specialist s", Specialist.class).getResultList(); }
}
