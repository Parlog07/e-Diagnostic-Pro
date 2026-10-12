package ma.teleexpertise.entity;

import jakarta.persistence.*;
import ma.teleexpertise.enums.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "nurse")
public class Nurse extends User {
    public Nurse() {}

}
