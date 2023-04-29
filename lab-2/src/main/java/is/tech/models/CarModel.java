package is.tech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name="car_model")
public class CarModel {
    @Id
    private long id;
    private String name;
    private int length;
    private int width;
    @Column(name="body_style")
    private String bodyStyle;
    @ManyToOne
    @JoinColumn(name="manufacturer_id", nullable = false)
    private CarManufacturer carManufacturer;
    @Column(name="manufacturer_id", updatable=false, insertable=false)
    private long manufacturerId;

    public CarModel() {}
}
