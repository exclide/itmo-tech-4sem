package is.tech.mappers;

import is.tech.dtos.CarManufacturerDto;
import is.tech.models.CarManufacturer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CarManufacturerMapper {
    CarManufacturerMapper INSTANCE = Mappers.getMapper(CarManufacturerMapper.class);

    CarManufacturerDto map(CarManufacturer carManufacturer);
    CarManufacturer map(CarManufacturerDto carManufacturerDto);
    List<CarManufacturerDto> carManufacturerListToDto(List<CarManufacturer> manus);
    List<CarManufacturer> carManufacturerDtoListToManufacturer(List<CarManufacturerDto> manus);

    CarManufacturerDto[] map(List<CarManufacturer> manus);
}
