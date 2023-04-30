package is.tech.repository;

import is.tech.models.CarEngine;
import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarEngineRepository extends JpaRepository<CarEngine, Long> {
    List<CarEngine> getAllByCarModelId(Long modelId);
    List<CarEngine> getAllByName(String modelName);
}
