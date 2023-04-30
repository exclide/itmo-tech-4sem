package is.tech.controller;

import is.tech.models.CarEngine;
import is.tech.repository.CarEngineRepository;
import is.tech.repository.CarEngineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarEngineController {

    CarEngineRepository repository;

    @Autowired
    public CarEngineController(CarEngineRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value="/carEngines")
    public List<CarEngine> getAllCarEngines() {
        return repository.findAll();
    }
    @GetMapping(value="/carEngines/{id}")
    public CarEngine getCarEngine(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping(value="/carEngines/engineName={name}")
    public List<CarEngine> getCarEnginesByName(@PathVariable String name) {
        return repository.getAllByName(name);
    }

    @GetMapping(value="/carEngines/modelId={parentId}")
    public List<CarEngine> getCarEnginesByManufacturerId(@PathVariable Long parentId) {
        return repository.getAllByCarModelId(parentId);
    }

    @PostMapping(value="/carEngines")
    public CarEngine postCarEngine(@RequestBody CarEngine engine) {
        return repository.save(engine);
    }

    @PutMapping(value="/carEngines/{id}")
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

    @DeleteMapping("/carEngines/{id}")
    void deleteCarEngine(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/carEngines")
    void deleteAllCarEngines() {
        repository.deleteAll();
    }
}