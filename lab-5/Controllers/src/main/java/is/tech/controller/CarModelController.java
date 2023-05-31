package is.tech.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;
import is.tech.repository.CarManufacturerRepository;
import is.tech.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class CarModelController {
    CarModelRepository repository;
    CarManufacturerRepository manufacturerRepository;

    @Autowired
    public CarModelController(CarModelRepository repository, CarManufacturerRepository manufacturerRepository) {
        this.repository = repository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @GetMapping(value="/api/carModels")
    @PostFilter("hasAuthority('SCOPE_ADMIN') or filterObject.carManufacturer.owner.username == authentication.name")
    public List<CarModel> getAllCarModels() {
        return repository.findAll();
    }
    @GetMapping(value="/api/carModels/{id}")
    @PostAuthorize("hasAuthority('SCOPE_ADMIN') or returnObject.carManufacturer.owner.username == authentication.name")
    public CarModel getCarModel(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping(value="/api/carModels/modelName={name}")
    @PostFilter("hasAuthority('SCOPE_ADMIN') or filterObject.carManufacturer.owner.username == authentication.name")
    public List<CarModel> getCarModelsByName(@PathVariable String name) {
        return repository.getAllByName(name);
    }

    @GetMapping(value="/api/carModels/manufacturerId={parentId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #parentId")
    public List<CarModel> getCarModelsByManufacturerId(@PathVariable Long parentId) {
        return repository.getAllByCarManufacturer_Id(parentId);
    }


    record modelBody(long id, String name, int length, int width, String bodyStyle, long manufacturerId) {};
    @PostMapping(value="/api/carModels")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #model.manufacturerId()")
    public CarModel postCarModel(@RequestBody modelBody model) {
        CarManufacturer carManufacturer = manufacturerRepository
                .findById(model.manufacturerId)
                .orElseThrow(() -> new SecurityException("manu not found"));

        CarModel carModel = new CarModel(model.id, model.name, model.length, model.width, model.bodyStyle, carManufacturer);
        return repository.save(carModel);
    }


    @PutMapping(value="/api/carModels/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #model.manufacturerId()")
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

        return repository.save(updateModel);
    }

    @DeleteMapping("/api/carModels/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or carModelRepository.getById(#id).carManufacturer.id = authentication.manufacturerId")
    void deleteCarModel(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/api/carModels")
    @Secured("SCOPE_ADMIN")
    void deleteAllCarModels() {
        repository.deleteAll();
    }

    @DeleteMapping("/api/carModels?carManufacturer={manufacturerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #manufacturerId")
    void deleteCarModelsByManufacturerId(@PathVariable Long manufacturerId) {
        repository.deleteAllByCarManufacturer_Id(manufacturerId);
    }
}