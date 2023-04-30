package is.tech.repository;

import is.tech.models.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    List<CarModel> getAllByManufacturerId(Long manufacturerId);
    List<CarModel> getAllByName(String modelName);
}
