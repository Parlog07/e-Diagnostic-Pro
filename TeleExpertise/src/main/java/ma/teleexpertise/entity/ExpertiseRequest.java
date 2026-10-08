package ma.teleexpertise.entity;

import ma.teleexpertise.enums.ExpertiseStatus;
import ma.teleexpertise.enums.Priority;
import jakarta.persistence.*;


@Entity
@Table(name = "expertise_requests")
public class ExpertiseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    @Enumerated(EnumType.STRING)
    private ma.teleexpertise.enums.Priority priority;
    @Enumerated(EnumType.STRING)
    private ma.teleexpertise.enums.ExpertiseStatus status;
    private String expertOpinion;
    private String recommendations;
    @OneToOne
    @JoinColumn(name = "consultation_id", unique = true, nullable = false)
    private Consultation consultation;
    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;
    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    public ExpertiseRequest() {
    }

    public ExpertiseRequest(Long id, String question, ma.teleexpertise.enums.Priority priority,
                            ma.teleexpertise.enums.ExpertiseStatus status, Consultation consultation,
                            Specialist specialist, TimeSlot timeSlot) {
        this.id = id;
        this.question = question;
        this.priority = priority;
        this.status = status;
        this.consultation = consultation;
        this.specialist = specialist;
        this.timeSlot = timeSlot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public ExpertiseStatus getStatus() {
        return status;
    }

    public void setStatus(ExpertiseStatus status) {
        this.status = status;
    }

    public String getExpertOpinion() {
        return expertOpinion;
    }

    public void setExpertOpinion(String expertOpinion) {
        this.expertOpinion = expertOpinion;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }
}