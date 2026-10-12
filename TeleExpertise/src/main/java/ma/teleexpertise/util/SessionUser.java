package ma.teleexpertise.util;

import ma.teleexpertise.entity.User;
import java.io.Serializable;

public class SessionUser implements Serializable {
    private final Long id;
    private final String name;
    private final String role;
    public SessionUser(User user) { id = user.getId(); name = user.getFullName(); role = user.getRole(); }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getHome() {
        if (role.equals("NURSE")) return "/nurse/patients";
        if (role.equals("DOCTOR")) return "/doctor/consultations";
        return "/specialist/requests";
    }
}
