package is.tech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Entity
@Table(name="car_manufacturer")
public class CarManufacturer {
    @Id
    private long id;
    private String name;
    private LocalDate date;

    public CarManufacturer() {}
}
