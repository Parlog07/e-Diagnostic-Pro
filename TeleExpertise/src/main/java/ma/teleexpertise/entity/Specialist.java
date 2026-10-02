package ma.teleexpertise.entity;

public class Specialist extends User {

    private String specialty;
    private double price;

    public Specialist() {
    }

    public Specialist(Long id, String firstName, String lastName, String email, String password, String specialty, double price) {
        super(id, firstName, lastName, email, password);
        this.specialty = specialty;
        this.price = price;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}patient