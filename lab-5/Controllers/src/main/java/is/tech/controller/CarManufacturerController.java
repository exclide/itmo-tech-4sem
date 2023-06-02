package is.tech.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import is.tech.dtos.CarManufacturerDto;
import is.tech.kafka.KafkaManufacturerService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@SecurityRequirement(name = "bearerAuth")
public class CarManufacturerController {
    KafkaManufacturerService kafkaService;

    public CarManufacturerController(KafkaManufacturerService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @GetMapping(value="/api/carManufacturers")
    @PostFilter("hasAuthority('SCOPE_ADMIN') or filterObject.owner.username == authentication.name")
    public List<CarManufacturerDto> getAllCarManufacturers() {
        return kafkaService.findAll();
    }

    @GetMapping(value="/api/carManufacturers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #id")
    public CarManufacturerDto getCarManufacturer(@PathVariable Long id) {
        return kafkaService.getById(id);
    }

    @GetMapping(value="/api/carManufacturers/manufacturerName={name}")
    @PostFilter("hasAuthority('SCOPE_ADMIN') or filterObject.owner.username == authentication.name")
    public List<CarManufacturerDto> getCarManufacturersByName(@PathVariable String name) {
        return kafkaService.findAllByName(name);
    }

    record manufacturerBody(long id, String name, LocalDate date, long owner_id) {};
    @PostMapping(value="/api/carManufacturers")
    @Secured("SCOPE_ADMIN")
    public CarManufacturerDto postCarManufacturer(@RequestBody CarManufacturerDto dto) {
        //User user = userService.findUserById(manufacturerBody.owner_id);
        //CarManufacturer manufacturer = new CarManufacturer(manufacturerBody.id, manufacturerBody.name, manufacturerBody.date, user);

        //user.setCarManufacturerId(manufacturer.getId());

        //userService.saveUser(user);

        return kafkaService.save(dto);
    }

    record manufacturerUpdateBody(String name, LocalDate date) {};
    @PutMapping(value="/api/carManufacturers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #id")
    public CarManufacturerDto updateCarManufacturer(@RequestBody CarManufacturerDto dto, @PathVariable Long id) {
        return kafkaService.save(dto);
    }

    @DeleteMapping("/api/carManufacturers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #id")
    public void deleteCarManufacturer(@PathVariable Long id) {
        kafkaService.deleteById(id);
    }

    @DeleteMapping("/api/carManufacturers")
    @Secured("SCOPE_ADMIN")
    public void deleteAllCarManufacturers() {
        kafkaService.deleteAll();
    }
}