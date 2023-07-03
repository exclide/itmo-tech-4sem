package is.tech.mappers;

import is.tech.dtos.CarManufacturerDto;
import is.tech.dtos.CarModelDto;
import is.tech.models.CarManufacturer;
import is.tech.models.CarModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CarModelMapper {
    CarModelMapper INSTANCE = Mappers.getMapper(CarModelMapper.class);

    CarModelDto map(CarModel carModel);
    CarModel map(CarModelDto carModelDto);

    CarModelDto[] map(List<CarModel> models);
}
