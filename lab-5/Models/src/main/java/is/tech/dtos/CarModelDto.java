package is.tech.dtos;

import is.tech.models.CarManufacturer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarModelDto {
    private Long id;
    private String name;
    private int length;
    private int width;
    private String bodyStyle;
    private CarManufacturerDto carManufacturer;
}