package ma.teleexpertise.entity;

import jakarta.persistence.Entity;
@Entity
public class Nurse extends User {

    public Nurse() {
    }

    public Nurse(Long id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password);
    }
    
}