package ma.teleexpertise.util;

import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Csrf {
    public static void create(HttpSession session) {
        byte[] bytes = new byte[32]; new SecureRandom().nextBytes(bytes);
        session.setAttribute("csrf", Base64.getUrlEncoder().withoutPadding().encodeToString(bytes));
    }
    public static boolean valid(HttpSession session, String submitted) {
        if (session == null || submitted == null) return false;
        String token = (String) session.getAttribute("csrf");
        return token != null && MessageDigest.isEqual(token.getBytes(StandardCharsets.UTF_8), submitted.getBytes(StandardCharsets.UTF_8));
    }
}
