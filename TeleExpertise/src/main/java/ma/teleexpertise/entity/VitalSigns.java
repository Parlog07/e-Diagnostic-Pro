package ma.teleexpertise.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "vital_signs")
public class VitalSigns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double systolicPressure;
    private double diastolicPressure;
    private int heartRate;
    private double temperature;
    private int respiratoryRate;
    private double weight;
    private double height;
    private LocalDateTime recordedAt;
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    public VitalSigns() {
    }

    public VitalSigns(Long id, double systolicPressure, double diastolicPressure,
                      int heartRate, double temperature, int respiratoryRate,
                      double weight, double height, LocalDateTime recordedAt,
                      Patient patient) {
        this.id = id;
        this.systolicPressure = systolicPressure;
        this.diastolicPressure = diastolicPressure;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.respiratoryRate = respiratoryRate;
        this.weight = weight;
        this.height = height;
        this.recordedAt = recordedAt;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(double systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public double getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(double diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(int respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}