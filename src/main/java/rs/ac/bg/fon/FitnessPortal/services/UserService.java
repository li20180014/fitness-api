package rs.ac.bg.fon.FitnessPortal.services;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserPostDto;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserProfileGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserProfilePutDto;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserPutDto;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;

import java.util.List;

@Service
public interface UserService {

    List<UserGetDto> get();
    UserGetDto get(String email);
    UserGetDto create(UserPostDto userPostDto, List<ApplicationUserRole> roleTypes);
    void delete(String email);
    UserGetDto update(UserPutDto userPutDto);

    UserProfileGetDto getWithProfile(String email);
    UserProfileGetDto updateWithProfile(String email, UserProfilePutDto profilePutDto);

}
