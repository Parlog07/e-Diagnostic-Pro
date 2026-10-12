package ma.teleexpertise.util;

import java.math.BigDecimal;

public class Validation {
    public static String required(String value, String label, int max) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " est obligatoire.");
        return optional(value, label, max);
    }
    public static String optional(String value, String label, int max) {
        if (value == null) return "";
        value = value.trim();
        if (value.length() > max) throw new IllegalArgumentException(label + " est trop long.");
        return value;
    }
    public static BigDecimal money(BigDecimal value) {
        if (value == null || value.signum() < 0 || value.compareTo(new BigDecimal("99999999.99")) > 0 || value.scale() > 2)
            throw new IllegalArgumentException("Montant invalide : positif, avec deux décimales maximum.");
        return value.setScale(2);
    }
}
