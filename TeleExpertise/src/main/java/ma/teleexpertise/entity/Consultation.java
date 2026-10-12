package ma.teleexpertise.entity;

import jakarta.persistence.*;
import ma.teleexpertise.enums.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "consultation")
public class Consultation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(optional = false) @JoinColumn(name = "doctor_id", nullable = false)
    private GeneralPractitioner doctor;

    @Column(nullable = false, length = 1000)
    private String reason;

    @Column(length = 3000)
    private String observations;

    @Column(length = 3000)
    private String diagnosis;

    @Column(length = 3000)
    private String treatment;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 50)
    private ConsultationStatus status = ConsultationStatus.EN_COURS;

    @Column(name = "base_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseCost = new BigDecimal("150.00");

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "consultation") @OrderBy("id ASC")
    private List<MedicalAct> medicalActs = new ArrayList<>();

    @OneToOne(mappedBy = "consultation")
    private ExpertiseRequest expertiseRequest;

    public Consultation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public GeneralPractitioner getDoctor() { return doctor; }
    public void setDoctor(GeneralPractitioner doctor) { this.doctor = doctor; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public ConsultationStatus getStatus() { return status; }
    public void setStatus(ConsultationStatus status) { this.status = status; }

    public BigDecimal getBaseCost() { return baseCost; }
    public void setBaseCost(BigDecimal baseCost) { this.baseCost = baseCost; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<MedicalAct> getMedicalActs() { return medicalActs; }
    public void setMedicalActs(List<MedicalAct> medicalActs) { this.medicalActs = medicalActs; }

    public ExpertiseRequest getExpertiseRequest() { return expertiseRequest; }
    public void setExpertiseRequest(ExpertiseRequest expertiseRequest) { this.expertiseRequest = expertiseRequest; }

}
