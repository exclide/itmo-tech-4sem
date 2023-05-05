package is.tech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="car_engine")
public class CarEngine {
    @Id
    private Long id;
    private String name;
    private int volume;
    private int cylinders;
    private int height;
    @ManyToOne
    @JoinColumn(name="model_id", nullable = false)
    private CarModel carModel;
}
