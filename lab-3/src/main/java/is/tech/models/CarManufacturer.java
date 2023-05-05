package is.tech.models;

import is.tech.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="car_manufacturer")
public class CarManufacturer {
    @Id
    private Long id;
    private String name;
    private LocalDate date;
    @OneToOne
    @JoinColumn(name="user_id")
    private User owner;

    @PreRemove
    private void removeFromOwner() {
        owner.setCarManufacturerId(null);
    }
}
