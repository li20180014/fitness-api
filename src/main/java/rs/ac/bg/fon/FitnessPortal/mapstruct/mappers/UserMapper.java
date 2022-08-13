package rs.ac.bg.fon.FitnessPortal.mapstruct.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.FitnessPortal.entities.User;
import rs.ac.bg.fon.FitnessPortal.security.authentication.MyUserDetails;
import rs.ac.bg.fon.FitnessPortal.dtos.user.*;

import java.util.List;

@Component
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserGetDto userToUserGetDto(User user);
    User userPostDtoToUser(UserPostDto userPostDto);
    List<UserGetDto> usersToUserGetDtos(List<User> users);

    @Mapping(target = "roles", ignore = true)
    void update(UserPutDto userPutDto, @MappingTarget User user);

    @Mapping(target = "username", source = "email")
    MyUserDetails userToMyUserDetails(User user);


}
