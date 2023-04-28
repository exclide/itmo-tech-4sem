package is.tech.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CarManufacturer {
    private long id;
    private String name;
    private LocalDate date;

    public CarManufacturer() {}
}
