package is.tech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name="car_engine")
public class CarEngine {
    @Id
    long id;
    String name;
    int volume;
    int cylinders;
    int height;
    @ManyToOne
    @JoinColumn(name="model_id", nullable = false)
    CarModel carModel;

    public CarEngine() {}
}
