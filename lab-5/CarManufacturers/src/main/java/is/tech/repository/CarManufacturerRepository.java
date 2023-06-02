package is.tech.repository;


import is.tech.models.CarManufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarManufacturerRepository extends JpaRepository<CarManufacturer, Long> {
    List<CarManufacturer> findAllByName(String manufacturerName);
}
