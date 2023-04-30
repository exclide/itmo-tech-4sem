package is.tech.repository;

import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarManufacturerRepository extends JpaRepository<CarManufacturer, Long> {
    List<CarManufacturer> getAllByName(String manufacturerName);
}
