package is.tech.dao;

import is.tech.models.CarManufacturer;

import java.sql.SQLException;
import java.util.List;

public interface CarManufacturerDao {
    CarManufacturer save(CarManufacturer entity) throws SQLException;
    void deleteById(long id) throws SQLException;
    void deleteByEntity(CarManufacturer entity) throws SQLException;
    void deleteAll() throws SQLException;
    CarManufacturer update(CarManufacturer entity) throws SQLException;
    CarManufacturer getById(long id) throws SQLException;
    List<CarManufacturer> getAll();
}
