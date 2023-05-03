package is.tech.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import is.tech.models.CarEngine;
import is.tech.repository.CarEngineRepository;
import is.tech.repository.CarEngineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class CarEngineController {

    CarEngineRepository repository;

    @Autowired
    public CarEngineController(CarEngineRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value="/api/carEngines")
    public List<CarEngine> getAllCarEngines() {
        return repository.findAll();
    }
    @GetMapping(value="/api/carEngines/{id}")
    public CarEngine getCarEngine(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping(value="/api/carEngines/engineName={name}")
    public List<CarEngine> getCarEnginesByName(@PathVariable String name) {
        return repository.getAllByName(name);
    }

    @GetMapping(value="/api/carEngines/modelId={parentId}")
    public List<CarEngine> getCarEnginesByManufacturerId(@PathVariable Long parentId) {
        return repository.getAllByCarModelId(parentId);
    }

    @PostMapping(value="/api/carEngines")
    public CarEngine postCarEngine(@RequestBody CarEngine engine) {
        return repository.save(engine);
    }

    @PutMapping(value="/api/carEngines/{id}")
    public CarEngine updateCarEngine(@RequestBody CarEngine engine, @PathVariable Long id) {
        var updateEngine = repository.findById(id).orElse(null);

        if (updateEngine == null) {
            engine.setId(id);
            return repository.save(engine);
        }

        updateEngine.setName(engine.getName());
        updateEngine.setVolume(engine.getVolume());
        updateEngine.setCylinders(engine.getCylinders());
        updateEngine.setHeight(engine.getHeight());
        updateEngine.setCarModel(engine.getCarModel());


        return repository.save(updateEngine);
    }

    @DeleteMapping("/api/carEngines/{id}")
    void deleteCarEngine(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/api/carEngines")
    void deleteAllCarEngines() {
        repository.deleteAll();
    }
}