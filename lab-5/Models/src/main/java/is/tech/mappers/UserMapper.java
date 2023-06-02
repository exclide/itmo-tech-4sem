package is.tech.mappers;

import is.tech.dtos.CarManufacturerDto;
import is.tech.dtos.UserDto;
import is.tech.models.CarManufacturer;
import is.tech.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    UserDto map(User user);
    User map(UserDto userDto);
}
