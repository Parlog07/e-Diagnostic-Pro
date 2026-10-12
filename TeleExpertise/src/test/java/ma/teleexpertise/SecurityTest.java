package ma.teleexpertise;

import ma.teleexpertise.util.Csrf;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {
    @Test void csrfRejectsMissingOrWrongToken() {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("csrf")).thenReturn("valid-token");
        assertFalse(Csrf.valid(null,"valid-token")); assertFalse(Csrf.valid(session,null));
        assertFalse(Csrf.valid(session,"wrong-token")); assertTrue(Csrf.valid(session,"valid-token"));
    }
}
