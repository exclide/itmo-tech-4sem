package is.tech.service;

import is.tech.models.CarManufacturer;
import is.tech.repository.CarManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarManufacturerService {
    CarManufacturerRepository repository;

    @Autowired
    public CarManufacturerService(CarManufacturerRepository repository) {
        this.repository = repository;
    }

    public List<CarManufacturer> findAll() {
        return repository.findAll();
    }

    public CarManufacturer getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SecurityException("No manufacturer with id: " + id));
    }

    public List<CarManufacturer> findAllByName(String manufacturerName) {
        return repository.findAllByName(manufacturerName);
    }

    public CarManufacturer save(CarManufacturer carManufacturer) {
        return repository.save(carManufacturer);
    }

    public CarManufacturer update(CarManufacturer manufacturer, long id) {
        var updateManufacturer = repository.findById(id).orElse(null);

        if (updateManufacturer == null) {
            manufacturer.setId(id);
            return repository.save(manufacturer);
        }

        updateManufacturer.setName(manufacturer.getName());
        updateManufacturer.setDate(manufacturer.getDate());

        return repository.save(updateManufacturer);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteById(long id) {
        var manufacturer = repository.findById(id)
                .orElseThrow(() -> new SecurityException("No manufacturer with id: " + id));

        repository.delete(manufacturer);
    }
}
