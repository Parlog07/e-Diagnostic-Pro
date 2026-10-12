package ma.teleexpertise;

import jakarta.persistence.*;
import ma.teleexpertise.entity.*;
import ma.teleexpertise.enums.*;
import ma.teleexpertise.service.*;
import ma.teleexpertise.util.AccessDeniedException;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PersistenceWorkflowTest {
    private EntityManagerFactory factory;
    @BeforeEach void setup() throws Exception {
        String url = "jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
        try (Connection connection = DriverManager.getConnection(url,"sa",""); Statement statement = connection.createStatement()) {
            for (String file : List.of("database/schema.sql","database/seed.sql")) {
                for (String sql : Files.readString(Path.of(file)).split(";")) if (!sql.isBlank()) statement.execute(sql.trim());
            }
        }
        factory = Persistence.createEntityManagerFactory("teleexpertise", Map.of("jakarta.persistence.jdbc.url",url,"jakarta.persistence.jdbc.user","sa","jakarta.persistence.jdbc.password",""));
    }
    @AfterEach void close() { if (factory != null) factory.close(); }
    private Patient patient() {
        Patient p = new Patient(); p.setFirstName("Test"); p.setLastName("Fictif"); p.setBirthDate(LocalDate.of(2000,1,1)); p.setSocialSecurityNumber("TEST-100"); return p;
    }
    private VitalSigns vitals() {
        VitalSigns v = new VitalSigns(); v.setBloodPressure("120/80"); v.setHeartRate(75); v.setTemperature(new BigDecimal("36.8")); v.setRespiratoryRate(16); return v;
    }
    @Test void completePersistedWorkflow() {
        AuthService auth = new AuthService(factory);
        assertEquals("NURSE",auth.login("nurse@demo.local","TeleDemo2026!").getRole());
        assertEquals("DOCTOR",auth.login("doctor@demo.local","TeleDemo2026!").getRole());
        assertEquals("SPECIALIST",auth.login("specialist@demo.local","TeleDemo2026!").getRole());
        assertNull(auth.login("doctor@demo.local","incorrect"));
        PatientService patients = new PatientService(factory);
        ConsultationService consultations = new ConsultationService(factory);
        SpecialistService specialists = new SpecialistService(factory);
        Long patientId = patients.register(patient(),vitals(),1L);
        assertThrows(IllegalArgumentException.class, () -> patients.register(patient(),vitals(),1L));
        assertEquals(1,patients.record(patientId,null,true).getVitalSigns().size());
        Long id = consultations.create(patientId,2L,"Motif fictif","Examen fictif");
        assertThrows(IllegalArgumentException.class, () -> consultations.create(patientId,2L,"Autre",""));
        assertThrows(AccessDeniedException.class, () -> consultations.details(id,5L));
        assertThrows(IllegalArgumentException.class, () -> patients.receive(patientId,vitals(),1L));
        assertThrows(IllegalArgumentException.class, () -> consultations.addAct(id,2L,"Test",new BigDecimal("-1")));
        consultations.addAct(id,2L,"Acte fictif",new BigDecimal("80.50"));
        consultations.request(id,2L,3L,"Question fictive",Priority.NORMALE);
        assertThrows(IllegalArgumentException.class, () -> consultations.request(id,2L,3L,"Doublon",Priority.NORMALE));
        assertThrows(IllegalArgumentException.class, () -> consultations.update(id,2L,"","Diagnostic","Traitement",true));
        Long requestId = consultations.details(id,2L).getExpertiseRequest().getId();
        assertThrows(AccessDeniedException.class, () -> specialists.details(requestId,4L));
        specialists.configure(3L,"Cardiologie",new BigDecimal("300.00"));
        assertEquals(new BigDecimal("480.50"),consultations.total(consultations.details(id,2L)));
        assertEquals(1,specialists.list(3L,ExpertiseStatus.EN_ATTENTE,Priority.NORMALE).size());
        specialists.respond(requestId,3L,"Avis fictif","Conseils fictifs");
        assertThrows(IllegalArgumentException.class, () -> specialists.respond(requestId,3L,"Deuxième","Avis"));
        assertEquals(ConsultationStatus.AVIS_RECU,consultations.details(id,2L).getStatus());
        consultations.update(id,2L,"Observations","Diagnostic fictif","Traitement fictif",true);
        assertEquals(ConsultationStatus.TERMINEE,consultations.details(id,2L).getStatus());
        assertEquals(new BigDecimal("480.50"),consultations.total(consultations.details(id,2L)));
        patients.receive(patientId,vitals(),1L);
        assertTrue(patients.record(patientId,null,true).isWaiting());
        assertEquals(2,patients.record(patientId,null,true).getVitalSigns().size());
    }
    @Test void directConsultationCanBeClosed() {
        ConsultationService service = new ConsultationService(factory);
        Long id = service.create(1L,2L,"Consultation directe","Observations");
        service.update(id,2L,"Observations","Diagnostic fictif","Traitement fictif",true);
        assertEquals(ConsultationStatus.TERMINEE,service.details(id,2L).getStatus());
        assertThrows(IllegalArgumentException.class, () -> service.addAct(id,2L,"Tardif",BigDecimal.ONE));
    }
}
