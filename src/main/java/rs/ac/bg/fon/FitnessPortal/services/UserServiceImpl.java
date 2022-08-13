package rs.ac.bg.fon.FitnessPortal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.FitnessPortal.dtos.user.*;
import rs.ac.bg.fon.FitnessPortal.entities.User;
import rs.ac.bg.fon.FitnessPortal.exception_handling.AdminCannotBeModifiedException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.EmailExistsException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.UserNotFoundException;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.UserMapper;
import rs.ac.bg.fon.FitnessPortal.repositories.UserRepository;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;
import rs.ac.bg.fon.FitnessPortal.security.configuration.InitialAdminConfiguration;
import rs.ac.bg.fon.FitnessPortal.services.utility.UserConfigurer;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private UserMapper userMapper;
    private InitialAdminConfiguration initialAdminConfig;
    private UserConfigurer userConfigurer;

    @Override
    public List<UserGetDto> get() {
        return userMapper.usersToUserGetDtos(userRepository.findAll());
    }

    @Override
    public UserGetDto get(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return userMapper.userToUserGetDto(user);
    }


    @Override
    @Transactional
    public UserGetDto create(UserPostDto userPostDto, List<ApplicationUserRole> roleTypes) {
        if(userRepository.existsByEmail(userPostDto.getEmail())) throw new EmailExistsException(userPostDto.getEmail());

        User user = userMapper.userPostDtoToUser(userPostDto);
        userConfigurer.addRoles(user, roleTypes);
        userConfigurer.encodePassword(user);
        user.setEnabled(true);

        userRepository.save(user);
        return userMapper.userToUserGetDto(user);
    }

    @Override
    @Transactional
    public UserGetDto update(UserPutDto userPutDto){
        if(userPutDto.getEmail().equals(initialAdminConfig.getEmail())) throw new AdminCannotBeModifiedException();

        User user = userRepository.findByEmail(userPutDto.getEmail()).orElseThrow(() -> new UserNotFoundException(userPutDto.getEmail()));

        userMapper.update(userPutDto, user);
        if(userPutDto.getPassword() != null) userConfigurer.encodePassword(user);
        userConfigurer.addRoles(user, userPutDto.getRoles());
        return userMapper.userToUserGetDto(userRepository.save(user));
    }


    @Override
    @Transactional
    public void delete(String email) {
        if(email.equals(initialAdminConfig.getEmail())) throw new AdminCannotBeModifiedException();

        if(!userRepository.existsByEmail(email)) throw new UserNotFoundException(email);
        userRepository.deleteByEmail(email);
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setInitialAdminConfig(InitialAdminConfiguration initialAdminConfig) {
        this.initialAdminConfig = initialAdminConfig;
    }

    @Autowired
    public void setUserConfigurer(UserConfigurer userConfigurer) {
        this.userConfigurer = userConfigurer;
    }
}