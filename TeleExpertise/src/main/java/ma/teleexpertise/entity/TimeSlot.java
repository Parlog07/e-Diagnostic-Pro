package ma.teleexpertise.entity;

import ma.teleexpertise.enums.TimeSlotStatus;

import java.time.LocalDateTime;

public class TimeSlot {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ma.teleexpertise.enums.TimeSlotStatus status;
    private Specialist specialist;

    public TimeSlot() {
    }

    public TimeSlot(Long id, LocalDateTime startTime, LocalDateTime endTime,
                    ma.teleexpertise.enums.TimeSlotStatus status, Specialist specialist) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.specialist = specialist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public TimeSlotStatus getStatus() {
        return status;
    }

    public void setStatus(TimeSlotStatus status) {
        this.status = status;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }
}