package is.tech.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarModel {
    private long id;
    private String name;
    private int length;
    private int width;
    private String bodyStyle;
    private CarManufacturer carManufacturer;
    private long manufacturerId;

    public CarModel() {}
}
