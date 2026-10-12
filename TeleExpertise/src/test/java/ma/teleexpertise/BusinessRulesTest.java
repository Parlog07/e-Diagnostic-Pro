package ma.teleexpertise;

import ma.teleexpertise.entity.*;
import ma.teleexpertise.enums.*;
import ma.teleexpertise.service.*;
import ma.teleexpertise.util.AccessDeniedException;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessRulesTest {
    private final EntityManagerFactory factory = mock(EntityManagerFactory.class);
    @Test void costCountsExpertiseOnce() {
        Consultation c = new Consultation();
        MedicalAct a = new MedicalAct(); a.setPrice(new BigDecimal("80.50"));
        MedicalAct b = new MedicalAct(); b.setPrice(new BigDecimal("20.00"));
        c.setMedicalActs(List.of(a,b));
        ExpertiseRequest r = new ExpertiseRequest(); r.setFee(new BigDecimal("250.00")); c.setExpertiseRequest(r);
        ConsultationService service = new ConsultationService(factory);
        assertEquals(new BigDecimal("500.50"), service.total(c));
        assertEquals(new BigDecimal("500.50"), service.total(c));
        r.setStatus(ExpertiseStatus.TERMINEE);
        assertEquals(new BigDecimal("500.50"), service.total(c));
        verifyNoInteractions(factory);
    }
    @Test void costWithoutExpertise() {
        assertEquals(new BigDecimal("150.00"), new ConsultationService(factory).total(new Consultation()));
    }
    @Test void invalidPatientIsRejected() {
        PatientService service = new PatientService(factory);
        Patient p = new Patient(); assertThrows(IllegalArgumentException.class, () -> service.validate(p));
        p.setFirstName(" Test "); p.setLastName("Demo"); p.setSocialSecurityNumber("TEST"); p.setBirthDate(LocalDate.now().plusDays(1));
        assertThrows(IllegalArgumentException.class, () -> service.validate(p));
        p.setBirthDate(LocalDate.of(2000,1,1)); assertDoesNotThrow(() -> service.validate(p));
        assertEquals("Test",p.getFirstName());
    }
    @Test void specialistsAreFilteredAndSorted() {
        Specialist expensive = new Specialist(); expensive.setSpecialty("Cardiologie"); expensive.setTariff(new BigDecimal("400"));
        Specialist cheap = new Specialist(); cheap.setSpecialty("Cardiologie"); cheap.setTariff(new BigDecimal("250"));
        Specialist other = new Specialist(); other.setSpecialty("Dermatologie"); other.setTariff(new BigDecimal("100"));
        List<Specialist> result = new SpecialistService(factory).filter(List.of(expensive,other,cheap),"CARDIO");
        assertEquals(List.of(cheap,expensive),result);
    }
    @Test void onlyAssignedSpecialistCanRespondAndOnlyOnce() {
        Specialist s = new Specialist(); s.setId(3L);
        ExpertiseRequest r = new ExpertiseRequest(); r.setSpecialist(s); r.setConsultation(new Consultation());
        r.getConsultation().setStatus(ConsultationStatus.EN_ATTENTE_AVIS_SPECIALISTE);
        SpecialistService service = new SpecialistService(factory);
        assertThrows(AccessDeniedException.class, () -> service.applyResponse(r,4L,"Avis","Conseils"));
        assertThrows(IllegalArgumentException.class, () -> service.applyResponse(r,3L,"","Conseils"));
        assertEquals(ExpertiseStatus.EN_ATTENTE,r.getStatus());
        service.applyResponse(r,3L,"Avis fictif","Conseils fictifs");
        assertEquals(ExpertiseStatus.TERMINEE,r.getStatus());
        assertEquals(ConsultationStatus.AVIS_RECU,r.getConsultation().getStatus());
        assertNotNull(r.getCompletedAt());
        assertThrows(IllegalArgumentException.class, () -> service.applyResponse(r,3L,"Autre avis","Conseils"));
    }
}
