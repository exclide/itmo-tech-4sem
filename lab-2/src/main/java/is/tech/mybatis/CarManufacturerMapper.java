package is.tech.mybatis;

import is.tech.models.CarManufacturer;

import java.util.List;

public interface CarManufacturerMapper {
    void save(CarManufacturer entity);
    void deleteById(long id);
    void deleteByEntity(CarManufacturer entity);
    void deleteAll();
    void update(CarManufacturer entity);
    CarManufacturer getById(long id);
    List<CarManufacturer> getAll();
}
