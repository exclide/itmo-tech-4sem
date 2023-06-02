package is.tech.mappers;

import is.tech.dtos.RoleDto;
import is.tech.dtos.UserDto;
import is.tech.models.Role;
import is.tech.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role map(RoleDto value);
    RoleDto map(Role value);

    Set<Role> map(List<RoleDto> roles);
    List<RoleDto> map(Set<Role> roles);
}
