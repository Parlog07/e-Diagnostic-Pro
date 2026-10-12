package ma.teleexpertise.entity;

import jakarta.persistence.*;
import ma.teleexpertise.enums.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "general_practitioner")
public class GeneralPractitioner extends User {
    @OneToMany(mappedBy = "doctor")
    private List<Consultation> consultations = new ArrayList<>();

    public GeneralPractitioner() {}

    public List<Consultation> getConsultations() { return consultations; }
    public void setConsultations(List<Consultation> consultations) { this.consultations = consultations; }

}
