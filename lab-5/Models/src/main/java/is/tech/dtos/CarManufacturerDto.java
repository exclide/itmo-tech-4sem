package is.tech.dtos;

import is.tech.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarManufacturerDto {
    private Long id;
    private String name;
    private LocalDate date;
    private UserDto owner;
}