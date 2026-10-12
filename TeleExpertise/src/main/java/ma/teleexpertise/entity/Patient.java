package ma.teleexpertise.entity;

import jakarta.persistence.*;
import ma.teleexpertise.enums.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "patient")
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "social_security_number", nullable = false, unique = true, length = 50)
    private String socialSecurityNumber;

    @Column(length = 30)
    private String phone;

    @Column(length = 300)
    private String address;

    @Column(length = 100)
    private String insurance;

    @Column(length = 3000)
    private String history;

    @Column(length = 1000)
    private String allergies;

    @Column(name = "current_treatment", length = 3000)
    private String currentTreatment;

    @Column(nullable = false)
    private boolean waiting = false;

    @Column(name = "arrival_at", nullable = false)
    private LocalDateTime arrivalAt;

    @OneToMany(mappedBy = "patient") @OrderBy("recordedAt DESC")
    private List<VitalSigns> vitalSigns = new ArrayList<>();

    @OneToMany(mappedBy = "patient") @OrderBy("createdAt DESC")
    private List<Consultation> consultations = new ArrayList<>();

    public Patient() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getSocialSecurityNumber() { return socialSecurityNumber; }
    public void setSocialSecurityNumber(String socialSecurityNumber) { this.socialSecurityNumber = socialSecurityNumber; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getInsurance() { return insurance; }
    public void setInsurance(String insurance) { this.insurance = insurance; }

    public String getHistory() { return history; }
    public void setHistory(String history) { this.history = history; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getCurrentTreatment() { return currentTreatment; }
    public void setCurrentTreatment(String currentTreatment) { this.currentTreatment = currentTreatment; }

    public boolean isWaiting() { return waiting; }
    public void setWaiting(boolean waiting) { this.waiting = waiting; }

    public LocalDateTime getArrivalAt() { return arrivalAt; }
    public void setArrivalAt(LocalDateTime arrivalAt) { this.arrivalAt = arrivalAt; }

    public List<VitalSigns> getVitalSigns() { return vitalSigns; }
    public void setVitalSigns(List<VitalSigns> vitalSigns) { this.vitalSigns = vitalSigns; }

    public List<Consultation> getConsultations() { return consultations; }
    public void setConsultations(List<Consultation> consultations) { this.consultations = consultations; }

    public String getFullName() { return firstName + " " + lastName; }
}
