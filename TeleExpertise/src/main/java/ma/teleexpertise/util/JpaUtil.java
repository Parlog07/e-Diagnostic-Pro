package ma.teleexpertise.util;

import jakarta.persistence.*;
import java.util.*;

public class JpaUtil {
    private static EntityManagerFactory factory;
    public static synchronized EntityManagerFactory getFactory() {
        if (factory == null) {
            Map<String, Object> values = new HashMap<>();
            values.put("jakarta.persistence.jdbc.url", setting("TE_DB_URL", "jdbc:postgresql://localhost:5432/teleexpertise_db"));
            values.put("jakarta.persistence.jdbc.user", setting("TE_DB_USER", null));
            values.put("jakarta.persistence.jdbc.password", setting("TE_DB_PASSWORD", null));
            factory = Persistence.createEntityManagerFactory("teleexpertise", values);
        }
        return factory;
    }
    private static String setting(String name, String fallback) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) value = fallback;
        if (value == null) throw new IllegalStateException("Variable manquante : " + name);
        return value;
    }
    public static synchronized void close() {
        if (factory != null) { factory.close(); factory = null; }
    }
}
