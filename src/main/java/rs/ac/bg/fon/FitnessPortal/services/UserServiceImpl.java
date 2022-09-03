package rs.ac.bg.fon.FitnessPortal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.FitnessPortal.dtos.user.*;
import rs.ac.bg.fon.FitnessPortal.entities.User;
import rs.ac.bg.fon.FitnessPortal.entities.UserProfileInformation;
import rs.ac.bg.fon.FitnessPortal.exception_handling.AdminCannotBeModifiedException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.EmailExistsException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.UserNotFoundException;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.UserMapper;
import rs.ac.bg.fon.FitnessPortal.repositories.UserRepository;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;
import rs.ac.bg.fon.FitnessPortal.security.configuration.InitialAdminConfiguration;
import rs.ac.bg.fon.FitnessPortal.services.utility.UserConfigurer;

import java.util.List;

/**
 * Represents a service layer class responsible for implementing all User related API methods.
 * Available API method implementations: POST, GET, PUT, DELETE
 *
 * @author Lana
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService{

    /**
     * Instance of User repository class, responsible for interacting with User database table.
     */
    private UserRepository userRepository;
    /**
     * Instance of Mapstruct Mapper class, responsible for mapping from DTO to DAO and vice versa.
     */
    private UserMapper userMapper;
    /**
     * Instance of Initial Admin Configurer class, responsible for providing data of the initial application admin.
     */
    private InitialAdminConfiguration initialAdminConfig;
    /**
     * Instance of User Configurer class, responsible for handling user configurations such as encoding passwords and setting application roles.
     */
    private UserConfigurer userConfigurer;

    /**
     * Returns a list of all available users in database.
     *
     * @return  List<UserGetDto> list of instances of UserGetDto class.
     */
    @Override
    public List<UserGetDto> get() {
        return userMapper.usersToUserGetDtos(userRepository.findAll());
    }

    /**
     * Returns a single user with provided email from database.
     *
     * @param email string value of user's email address.
     * @return UserGetDto instance of UserGetDto class representing the user with provided email.
     */
    @Override
    public UserGetDto get(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return userMapper.userToUserGetDto(user);
    }


    /**
     * Adds new user to database. Handles password encoding and setting the users allowed application roles.
     *
     * @param userPostDto instance of UserPostDto class with new user data.
     * @param roleTypes list of ApplicationUserRoles representing the user's application roles.
     * @return UserGetDto instance of UserGetDto class, representing the newly created user in database.
     * @throws EmailExistsException if user with email already exists in database.
     */
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

    /**
     * Updates existing user in database.
     *
     * @param userPutDto instance of UserPostDto class with new user data to update.
     * @return UserGetDto instance of UserGetDto class representing the updated user from the database.
     * @throws AdminCannotBeModifiedException if the user is trying to update the initial application admin.
     * @throws UserNotFoundException if user with email doesnt exist in database.
     */
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

    /**
     * Returns a user with filled User profile information. If user doesn't have existing profile information, fills his profile with provided information.
     *
     * @param email string value of user's email address.
     * @return UserProfileGetDto instance of UserProfileGetDto class representing the user with profile information.
     * @throws UserNotFoundException if user with email doesnt exist in database.
     */
    @Override
    public UserProfileGetDto getWithProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        UserProfileGetDto userProfileGetDto = userMapper.userToUserProfileGetDto(user);

        if(user.getUserProfileInformation() != null) fillProfileInfo(userProfileGetDto, user.getUserProfileInformation());

        return userProfileGetDto;
    }

    /**
     * Updates user profile information. If user doesn't have an existing profile, creates new profile, otherwise updates the existing
     * profile information with new provided data.
     *
     * @param email string value of user's email address/
     * @param profilePutDto instance of ProfilePutDto class with new data to update.
     * @return UserProfileGetDto instance of UserProfileGetDto class representing the updated user with profile information.
     * @throws AdminCannotBeModifiedException if the user is trying to update the initial application admin.
     * @throws UserNotFoundException if user with email doesn't exist in database.
     */
    @Override
    @Transactional
    public UserProfileGetDto updateWithProfile(String email, UserProfilePutDto profilePutDto) {
        if(email.equals(initialAdminConfig.getEmail())) throw new AdminCannotBeModifiedException();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        userMapper.updateWithProfile(profilePutDto, user);
        if(profilePutDto.getPassword() != null) userConfigurer.encodePassword(user);

        if(user.getUserProfileInformation() == null) {
            createNewProfileInfo(user, profilePutDto);
        }else {
            updateExistingProfileInfo(user, profilePutDto);
        }

        UserProfileGetDto userProfileGetDto = userMapper.userToUserProfileGetDto(user);

        if(user.getUserProfileInformation() != null) fillProfileInfo(userProfileGetDto, user.getUserProfileInformation());

        userRepository.save(user);

        return userProfileGetDto;
    }


    /**
     * Deletes user by provided email from database.
     *
     * @param email string value of users email address.
     * @throws AdminCannotBeModifiedException if the user is trying to delete the initial application admin.
     * @throws UserNotFoundException if user with email doesn't exist in database.
     */
    @Override
    @Transactional
    public void delete(String email) {
        if(email.equals(initialAdminConfig.getEmail())) throw new AdminCannotBeModifiedException();

        if(!userRepository.existsByEmail(email)) throw new UserNotFoundException(email);
        userRepository.deleteByEmail(email);
    }

    /**
     * Sets user repository to provided instance of UserRepository class.
     *
     * @param userRepository new Object instance of UserRepository class.
     */
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Sets user mapper to provided instance of UserMapper class.
     *
     * @param userMapper new Object instance of mapstruct UserMapper class.
     */
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Sets initial Admin Config to provided instance of InitialAdminConfig class.
     *
     * @param initialAdminConfig new Object instance of InitialAdminConfig class.
     */
    @Autowired
    public void setInitialAdminConfig(InitialAdminConfiguration initialAdminConfig) {
        this.initialAdminConfig = initialAdminConfig;
    }

    /**
     * Sets user configurer to provided instance of UserConfigurer class.
     *
     * @param userConfigurer new Object instance of UserConfigurer class.
     */
    @Autowired
    public void setUserConfigurer(UserConfigurer userConfigurer) {
        this.userConfigurer = userConfigurer;
    }

    /**
     * Fills user's profile information with provided profile data.
     *
     * @param userProfileGetDto instance of UserProfileGetDto class representing the user with profile which needs to be filled with data.
     * @param userProfileInformation instance of UserProfileInformation class, representing the new profile data.
     */
    private void fillProfileInfo(UserProfileGetDto userProfileGetDto, UserProfileInformation userProfileInformation) {
        userProfileGetDto.setAge(userProfileInformation.getAge());
        userProfileGetDto.setGender(userProfileInformation.getGender());
        userProfileGetDto.setHeight(userProfileInformation.getHeight());
        userProfileGetDto.setWeight(userProfileInformation.getWeight());
    }

    /**
     * Updates existing profile information with new data.
     *
     * @param user instance of class User whose profile information needs to be updated.
     * @param profilePutDto instance of ProfilePutDto with new profile data.
     */
    private void updateExistingProfileInfo(User user, UserProfilePutDto profilePutDto) {
        UserProfileInformation profileInfo = user.getUserProfileInformation();

        if (profilePutDto.getHeight() != null) profileInfo.setHeight(profilePutDto.getHeight());
        if (profilePutDto.getWeight() != null) profileInfo.setWeight(profilePutDto.getWeight());
        if (profilePutDto.getAge() != null) profileInfo.setAge(profilePutDto.getAge());
        if (profilePutDto.getGender() != null) profileInfo.setGender(profilePutDto.getGender());
    }

    /**
     * Method responsible for creating a new profile from provided User with provided data.
     *
     * @param user instance of User class whose profile needs to be created.
     * @param profilePutDto nstance of ProfilePutDto class with profile information.
     */
    private void createNewProfileInfo(User user, UserProfilePutDto profilePutDto) {
        UserProfileInformation profileInfo =
                new UserProfileInformation(profilePutDto.getHeight(), profilePutDto.getWeight(), profilePutDto.getAge(), profilePutDto.getGender());

        profileInfo.setUser(user);
    }

}
