package is.tech.controller;

import is.tech.models.CarManufacturer;
import is.tech.repository.CarManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
public class CarManufacturerController {

    CarManufacturerRepository repository;

    @Autowired
    public CarManufacturerController(CarManufacturerRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value="/carManufacturers")
    public List<CarManufacturer> getAllCarManufacturers() {
        return repository.findAll();
    }

    @GetMapping(value="/carManufacturers/{id}")
    public CarManufacturer getCarManufacturer(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping(value="/carManufacturers/manufacturerName={name}")
    public List<CarManufacturer> getCarManufacturersByName(@PathVariable String name) {
        return repository.getAllByName(name);
    }

    @PostMapping(value="/carManufacturers")
    public CarManufacturer postCarManufacturer(@RequestBody CarManufacturer manufacturer) {
        return repository.save(manufacturer);
    }

    @PutMapping(value="/carManufacturers/{id}")
    public CarManufacturer updateCarManufacturer(@RequestBody CarManufacturer manufacturer, @PathVariable Long id) {
        var updateManufacturer = repository.findById(id).orElse(null);

        if (updateManufacturer == null) {
            manufacturer.setId(id);
            return repository.save(manufacturer);
        }

        updateManufacturer.setName(manufacturer.getName());
        updateManufacturer.setDate(manufacturer.getDate());

        return repository.save(updateManufacturer);
    }

    @DeleteMapping("/carManufacturers/{id}")
    void deleteCarManufacturer(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/carManufacturers")
    void deleteAllCarManufacturers() {
        repository.deleteAll();
    }
}