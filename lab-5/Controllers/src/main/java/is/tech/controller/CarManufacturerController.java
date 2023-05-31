package is.tech.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import is.tech.auth.UserDetailsServiceImpl;
import is.tech.models.CarManufacturer;
import is.tech.repository.CarManufacturerRepository;
import is.tech.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@SecurityRequirement(name = "bearerAuth")
public class CarManufacturerController {

    CarManufacturerRepository repository;
    UserDetailsServiceImpl userService;

    @Autowired
    public CarManufacturerController(CarManufacturerRepository repository, UserDetailsServiceImpl userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping(value="/api/carManufacturers")
    @PostFilter("hasAuthority('SCOPE_ADMIN') or filterObject.owner.username == authentication.name")
    public List<CarManufacturer> getAllCarManufacturers() {
        return repository.findAll();
    }

    @GetMapping(value="/api/carManufacturers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #id")
    public CarManufacturer getCarManufacturer(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping(value="/api/carManufacturers/manufacturerName={name}")
    @PostFilter("hasAuthority('SCOPE_ADMIN') or filterObject.owner.username == authentication.name")
    public List<CarManufacturer> getCarManufacturersByName(@PathVariable String name) {
        return repository.getAllByName(name);
    }

    record manufacturerBody(long id, String name, LocalDate date, long owner_id) {};
    @PostMapping(value="/api/carManufacturers")
    @Secured("SCOPE_ADMIN")
    public CarManufacturer postCarManufacturer(@RequestBody manufacturerBody manufacturerBody) {
        User user = userService.findUserById(manufacturerBody.owner_id);
        CarManufacturer manufacturer = new CarManufacturer(manufacturerBody.id, manufacturerBody.name, manufacturerBody.date, user);

        user.setCarManufacturerId(manufacturer.getId());

        userService.saveUser(user);

        return repository.save(manufacturer);
    }

    record manufacturerUpdateBody(String name, LocalDate date) {};
    @PutMapping(value="/api/carManufacturers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #id")
    public CarManufacturer updateCarManufacturer(@RequestBody manufacturerUpdateBody manufacturerBody, @PathVariable Long id) {
        var updateManufacturer = repository.findById(id).orElseThrow(() -> new SecurityException("Manufacturer not found"));


        updateManufacturer.setName(manufacturerBody.name);
        updateManufacturer.setDate(manufacturerBody.date);

        return repository.save(updateManufacturer);
    }

    @DeleteMapping("/api/carManufacturers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or authentication.manufacturerId == #id")
    public void deleteCarManufacturer(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/api/carManufacturers")
    @Secured("SCOPE_ADMIN")
    public void deleteAllCarManufacturers() {
        repository.deleteAll();
    }
}