package is.tech.dao;

import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;

import java.sql.SQLException;
import java.util.List;

public interface CarModelDao {
    CarModel save(CarModel entity) throws SQLException;
    void deleteById(long id);
    void deleteByEntity(CarModel entity);
    void deleteAll();
    CarModel update(CarModel entity);
    CarModel getById(long id);
    List<CarModel> getAll();
    List<CarModel> getAllByVId(long id);
}
