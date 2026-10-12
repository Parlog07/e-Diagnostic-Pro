package ma.teleexpertise.service;

import jakarta.persistence.*;
import ma.teleexpertise.entity.*;
import ma.teleexpertise.repository.UserRepository;
import ma.teleexpertise.util.JpaUtil;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final EntityManagerFactory factory;
    public AuthService() { this(JpaUtil.getFactory()); }
    public AuthService(EntityManagerFactory factory) { this.factory = factory; }
    public User login(String email, String password) {
        if (email == null || password == null || email.length() > 200 || password.length() > 72) return null;
        try (EntityManager em = factory.createEntityManager()) {
            User user = new UserRepository(em).findByEmail(email.trim());
            if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) return user;
            return null;
        }
    }
}
