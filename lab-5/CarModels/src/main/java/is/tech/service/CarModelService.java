package is.tech.service;

import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;
import is.tech.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class CarModelService {
    CarModelRepository repository;

    @Autowired
    public CarModelService(CarModelRepository repository) {
        this.repository = repository;
    }

    public List<CarModel> findAll() {
        return repository.findAll();
    }

    public CarModel getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SecurityException("No model with id: " + id));
    }

    public List<CarModel> findAllByName(String modelName) {
        return repository.findAllByName(modelName);
    }

    public CarModel save(CarModel carModel) {
        return repository.save(carModel);
    }

    public CarModel update(CarModel carModel, long id) {
        var updateModel = repository.findById(id).orElse(null);

        if (updateModel == null) {
            carModel.setId(id);
            return repository.save(carModel);
        }

        updateModel.setLength(carModel.getLength());
        updateModel.setWidth(carModel.getWidth());
        updateModel.setName(carModel.getName());
        updateModel.setBodyStyle(carModel.getBodyStyle());
        updateModel.setCarManufacturer(updateModel.getCarManufacturer());

        return repository.save(updateModel);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteById(long id) {
        var model = repository.findById(id)
                .orElseThrow(() -> new SecurityException("No model with id: " + id));

        repository.delete(model);
    }

    public List<CarModel> findCarModelsByManufacturerId(Long parentId) {
        return repository.findAllByCarManufacturerId(parentId);
    }

    public void deleteCarModelsByManufacturerId(Long manufacturerId) {
        repository.deleteAllByCarManufacturerId(manufacturerId);
    }
}
