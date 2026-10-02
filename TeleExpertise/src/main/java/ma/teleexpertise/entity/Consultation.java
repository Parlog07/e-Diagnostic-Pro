package ma.teleexpertise.entity;

import java.time.LocalDateTime;

public class Consultation {

    private Long id;
    private String reason;
    private String observations;
    private String diagnosis;
    private String treatment;
    private double cost;
    private String status;
    private LocalDateTime createdAt;
    private Patient patient;
    private GeneralPractitioner generalPractitioner;

    public Consultation() {
    }

    public Consultation(Long id, String reason, String observations,
                        String diagnosis, String treatment, String status,
                        LocalDateTime createdAt, Patient patient,
                        GeneralPractitioner generalPractitioner) {
        this.id = id;
        this.reason = reason;
        this.observations = observations;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.cost = 150.0;
        this.status = status;
        this.createdAt = createdAt;
        this.patient = patient;
        this.generalPractitioner = generalPractitioner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public double getCost() {
        return cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public GeneralPractitioner getGeneralPractitioner() {
        return generalPractitioner;
    }

    public void setGeneralPractitioner(GeneralPractitioner generalPractitioner) {
        this.generalPractitioner = generalPractitioner;
    }
}