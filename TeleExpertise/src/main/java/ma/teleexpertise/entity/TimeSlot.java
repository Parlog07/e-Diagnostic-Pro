package ma.teleexpertise.entity;

import jakarta.persistence.*;
import ma.teleexpertise.enums.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "time_slot")
public class TimeSlot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private TimeSlotStatus status = TimeSlotStatus.DISPONIBLE;

    public TimeSlot() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Specialist getSpecialist() { return specialist; }
    public void setSpecialist(Specialist specialist) { this.specialist = specialist; }

    public LocalDateTime getStartsAt() { return startsAt; }
    public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }

    public TimeSlotStatus getStatus() { return status; }
    public void setStatus(TimeSlotStatus status) { this.status = status; }

}
