package is.tech.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;
import is.tech.repository.CarManufacturerRepository;
import is.tech.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class CarModelController {

    CarModelRepository repository;

    @Autowired
    public CarModelController(CarModelRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value="/api/carModels")
    public List<CarModel> getAllCarModels() {
        return repository.findAll();
    }
    @GetMapping(value="/api/carModels/{id}")
    public CarModel getCarModel(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping(value="/api/carModels/modelName={name}")
    public List<CarModel> getCarModelsByName(@PathVariable String name) {
        return repository.getAllByName(name);
    }

    @GetMapping(value="/api/carModels/manufacturerId={parentId}")
    public List<CarModel> getCarModelsByManufacturerId(@PathVariable Long parentId) {
        return repository.getAllByManufacturerId(parentId);
    }

    @PostMapping(value="/api/carModels")
    public CarModel postCarModel(@RequestBody CarModel model) {
        return repository.save(model);
    }

    @PutMapping(value="/api/carModels/{id}")
    public CarModel updateCarModel(@RequestBody CarModel model, @PathVariable Long id) {
        var updateModel = repository.findById(id).orElse(null);

        if (updateModel == null) {
            model.setId(id);
            return repository.save(model);
        }

        updateModel.setLength(model.getLength());
        updateModel.setWidth(model.getWidth());
        updateModel.setName(model.getName());
        updateModel.setBodyStyle(model.getBodyStyle());
        updateModel.setCarManufacturer(updateModel.getCarManufacturer());
        updateModel.setManufacturerId(updateModel.getManufacturerId());

        return repository.save(updateModel);
    }

    @DeleteMapping("/api/carModels/{id}")
    void deleteCarModel(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/api/carModels")
    void deleteAllCarModels() {
        repository.deleteAll();
    }
}