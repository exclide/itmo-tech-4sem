package is.tech.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import is.tech.dtos.CarModelDto;
import is.tech.kafka.KafkaModelService;
import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;
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
    KafkaModelService kafkaService;

    @Autowired
    public CarModelController(KafkaModelService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @GetMapping(value="/api/carModels")
    @PostFilter("hasAuthority('SCOPE_ADMIN') or filterObject.carManufacturer.owner.username == authentication.name")
    public List<CarModelDto> getAllCarModels() {
        return kafkaService.findAll();
    }
    @GetMapping(value="/api/carModels/{id}")
    @PostAuthorize("hasAuthority('SCOPE_ADMIN') or returnObject.carManufacturer.owner.username == authentication.name")
    public CarModelDto getCarModel(@PathVariable Long id) {
        return kafkaService.getById(id);
    }

    @GetMapping(value="/api/carModels/modelName={name}")
    @PostFilter("hasAuthority('SCOPE_ADMIN') or filterObject.carManufacturer.owner.username == authentication.name")
    public List<CarModelDto> getCarModelsByName(@PathVariable String name) {
        return kafkaService.findAllByName(name);
    }

    @GetMapping(value="/api/carModels/manufacturerId={parentId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #parentId")
    public List<CarModelDto> getCarModelsByManufacturerId(@PathVariable Long parentId) {
        return kafkaService.findAllByCarManufacturerId(parentId);
    }


    record modelBody(long id, String name, int length, int width, String bodyStyle, long manufacturerId) {};
    @PostMapping(value="/api/carModels")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #model.manufacturerId()")
    public CarModelDto postCarModel(@RequestBody CarModelDto carModel) {
        return kafkaService.save(carModel);
    }


    @PutMapping(value="/api/carModels/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #model.manufacturerId()")
    public CarModelDto updateCarModel(@RequestBody CarModelDto carModel, @PathVariable Long id) {
        return kafkaService.save(carModel);
    }

    @DeleteMapping("/api/carModels/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or carModelRepository.getById(#id).carManufacturer.id = authentication.manufacturerId")
    void deleteCarModel(@PathVariable Long id) {
        kafkaService.deleteById(id);
    }

    @DeleteMapping("/api/carModels")
    @Secured("SCOPE_ADMIN")
    void deleteAllCarModels() {
        kafkaService.deleteAll();
    }

    @DeleteMapping("/api/carModels?carManufacturer={manufacturerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #manufacturerId")
    void deleteCarModelsByManufacturerId(@PathVariable Long manufacturerId) {
        kafkaService.deleteAllByCarManufacturerId(manufacturerId);
    }
}