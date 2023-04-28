package is.tech.mybatis;

import is.tech.models.CarModel;


import java.util.List;

public interface CarModelMapper {
    void save(CarModel entity);
    void deleteById(long id);
    void deleteByEntity(CarModel entity);
    void deleteAll();
    void update(CarModel entity);
    CarModel getById(long id);
    List<CarModel> getAll();
    List<CarModel> getAllByVId(long id);
}
