package ma.teleexpertise.entity;

import jakarta.persistence.*;
import ma.teleexpertise.enums.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "specialist")
public class Specialist extends User {
    @Column(nullable = false, length = 100)
    private String specialty;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tariff;

    @OneToMany(mappedBy = "specialist")
    private List<ExpertiseRequest> requests = new ArrayList<>();

    public Specialist() {}

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public BigDecimal getTariff() { return tariff; }
    public void setTariff(BigDecimal tariff) { this.tariff = tariff; }

    public List<ExpertiseRequest> getRequests() { return requests; }
    public void setRequests(List<ExpertiseRequest> requests) { this.requests = requests; }

}
