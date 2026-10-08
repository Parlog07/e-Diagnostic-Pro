package ma.teleexpertise.entity;

import jakarta.persistence.Entity;

@Entity
public class GeneralPractitioner extends User {

    public GeneralPractitioner() {
    }

    public GeneralPractitioner(Long id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password);
    }
}