package rs.ac.bg.fon.FitnessPortal.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.FitnessPortal.dtos.user.*;
import rs.ac.bg.fon.FitnessPortal.entities.Gender;
import rs.ac.bg.fon.FitnessPortal.entities.Role;
import rs.ac.bg.fon.FitnessPortal.entities.User;
import rs.ac.bg.fon.FitnessPortal.entities.UserProfileInformation;
import rs.ac.bg.fon.FitnessPortal.exception_handling.AdminCannotBeModifiedException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.EmailExistsException;
import rs.ac.bg.fon.FitnessPortal.exception_handling.UserNotFoundException;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.UserMapperImpl;
import rs.ac.bg.fon.FitnessPortal.repositories.UserRepository;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;
import rs.ac.bg.fon.FitnessPortal.security.configuration.InitialAdminConfiguration;
import rs.ac.bg.fon.FitnessPortal.services.utility.UserConfigurer;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapperImpl userMapper;

    @InjectMocks
    private UserServiceImpl userService ;

    @Mock
    private InitialAdminConfiguration initialAdminConfig;

    @Mock
    private UserConfigurer userConfigurer;

    User user;

    @BeforeEach
    void setUp() {
         user = new User(1, "Lana", "Ilic", "lana.ilic99@gmail.com", "123");

    }

    @AfterEach
    void tearDown() {
    user = null;
    }


    @Test
    void getAllShouldReturnAllUsers() {
        List<User> predefinedList = new ArrayList<>();

        User user1 = new User(1, "Lana", "Ilic", "lana@gmail.com", "jfnsf");
        User user2 = new User(2, "Lana", "Ilic", "lana@gmail.com", "jfnsf");

        predefinedList.add(user1);
        predefinedList.add(user2);

        when(userRepository.findAll()).thenReturn(predefinedList);

        List<UserGetDto> fetchedList = userService.get();

        assertThat(fetchedList).usingRecursiveComparison().isEqualTo(predefinedList);
    }

    @Test
    void getByEmailShouldReturnUserByEmail(){

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserGetDto fetchedUser = userService.get(user.getEmail());
        assertThat(fetchedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void willThrowWhenUserDoesntExist(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.get("lana.ilic99@gmail.com");
        });
    }

    @Test
    void createUserShouldAddUser() {
        UserPostDto userPostDto = new UserPostDto(1, "Lana", "Ilic", "lana@gmail.com", "jfnsf");

        List<ApplicationUserRole> roleTypes = List.of(ApplicationUserRole.USER);

        doAnswer(invocation -> {
            User user = (User) invocation.getArgument(0);
            List<ApplicationUserRole> roleTypes1 =(List<ApplicationUserRole>) invocation.getArgument(1);

            Set<Role> roles = createRoles(roleTypes1);

            user.setRoles(roles);

            return null;
        }).when(userConfigurer).addRoles(any(User.class), any(List.class));

        doAnswer(invocation -> {
            User user = (User) invocation.getArgument(0);
            user.setPassword("123");
            return null;
        }).when(userConfigurer).encodePassword(any(User.class));

        userService.create(userPostDto, roleTypes);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        User userWithRole = userMapper.userPostDtoToUser(userPostDto);
        userWithRole.addRole(new Role(ApplicationUserRole.USER));
        userWithRole.setPassword("123");
        userWithRole.setEnabled(true);

        assertThat(userWithRole).usingRecursiveComparison().isEqualTo(capturedUser);
    }

    private Set<Role> createRoles(List<ApplicationUserRole> roleTypes1) {
        Set<Role> roles = new HashSet<>();

        for (ApplicationUserRole rt: roleTypes1) {
            roles.add(new Role(rt));
        }
        return roles;
    }

    @Test
    void willThrowWhenUserAlreadyExists(){
        UserPostDto userPostDto = new UserPostDto(1, "Lana", "Ilic", "lana@gmail.com", "jfnsf");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        Assertions.assertThrows(EmailExistsException.class, () -> {
            userService.create(userPostDto, null);
        });
    }


    @Test
    void updateByNameShouldUpdateSomeFieldsOfUserIncludingPassword(){
        UserPutDto newUser = new UserPutDto("Iva", null, "lana@gmail.com", "jfnsf", null);

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(user));

        doAnswer(invocation -> {
            User user1 = (User) invocation.getArgument(0);
            user1.setPassword("123");
            return null;
        }).when(userConfigurer).encodePassword(any(User.class));

        when(userRepository.save(user)).thenReturn(user);
        when(initialAdminConfig.getEmail()).thenReturn("");

        UserGetDto updatedUser = userService.update(newUser);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(user.getPassword()).isEqualTo(capturedUser.getPassword());
        assertThat(userMapper.userToUserGetDto(user)).usingRecursiveComparison().isEqualTo(updatedUser);
    }


    @Test
    void updateByNameShouldUpdateSomeFieldsOfUserExcludingPassword(){
        UserPutDto newUser = new UserPutDto("Iva", null, "lana@gmail.com", null, null);

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(initialAdminConfig.getEmail()).thenReturn("");

        UserGetDto updatedUser = userService.update(newUser);

        assertThat(userMapper.userToUserGetDto(user)).usingRecursiveComparison().isEqualTo(updatedUser);

    }

    @Test
    void willThrowWhenTryingToUpdateInitialAdmin(){
        UserPutDto newUser = new UserPutDto("Lana", "Ilic", "lana.ilic99@gmail.com", "lana123", null);

        when(initialAdminConfig.getEmail()).thenReturn("lana.ilic99@gmail.com");

        Assertions.assertThrows(AdminCannotBeModifiedException.class, () -> {
            userService.update(newUser);
        });
    }

    @Test
    void willThrowWhenUserDoesNotExist(){
        UserPutDto newUser = new UserPutDto("Lana", "Ilic", "lana.ilic99@gmail.com", "lana123", null);

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.update(newUser);
        });
    }

    @Test
    void getWithProfileShouldReturn() {

        when(userRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.of(user));

        UserProfileInformation upi = new UserProfileInformation(165, 57, 23, Gender.FEMALE);
        user.setUserProfileInformation(upi);

        UserProfileGetDto userProfileGetDto = userMapper.userToUserProfileGetDto(user);
        userProfileGetDto.setAge(upi.getAge());
        userProfileGetDto.setHeight(upi.getHeight());
        userProfileGetDto.setWeight(upi.getWeight());
        userProfileGetDto.setGender(upi.getGender());

        UserProfileGetDto userWithProfile = userService.getWithProfile("lana.ilic99@gmail.com");
        assertThat(userWithProfile).usingRecursiveComparison().isEqualTo(userProfileGetDto);

    }

    @Test
    void getWithProfileWillThrowDoesNotExist() {
        when(userRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getWithProfile("lana.ilic99@gmail.com");
        });
    }

    @Test
    void willThrowWhenTryingToUpdateInitialAdminProfile(){
        UserProfilePutDto newUser = new UserProfilePutDto("Lana", "Ilic", "123", 165, 57, 23, Gender.FEMALE);

        when(initialAdminConfig.getEmail()).thenReturn("lana.ilic99@gmail.com");

        Assertions.assertThrows(AdminCannotBeModifiedException.class, () -> {
            userService.updateWithProfile("lana.ilic99@gmail.com", newUser);
        });
    }

    @Test
    void willThrowWhenUserDoesNotExistWithProfile(){
        UserProfilePutDto newUser = new UserProfilePutDto("Lana", "Ilic", "123", 165, 57, 23, Gender.FEMALE);

        when(userRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.updateWithProfile("lana.ilic99@gmail.com", newUser);
        });
    }

    @Test
    void willUpdateWithProfile(){
        when(userRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.of(user));

        UserProfilePutDto profilePutDto = new UserProfilePutDto("Lana", "Ilic", null, 160, 55, 23, Gender.FEMALE);

        UserProfileInformation upi = new UserProfileInformation(165, 57, 23, Gender.FEMALE);
        user.setUserProfileInformation(upi);

        UserProfileGetDto userProfileGetDtoExpected = userMapper.userToUserProfileGetDto(user);
        userProfileGetDtoExpected.setGender(Gender.FEMALE);
        userProfileGetDtoExpected.setAge(23);
        userProfileGetDtoExpected.setWeight(55);
        userProfileGetDtoExpected.setHeight(160);


        UserProfileGetDto userProfileGetDto = userService.updateWithProfile("lana.ilic99@gmail.com", profilePutDto);

        assertThat(userProfileGetDtoExpected).usingRecursiveComparison().isEqualTo(userProfileGetDto);
    }

    @Test
    void willUpdateWithProfileCreateNewProfile(){
        when(userRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.of(user));

        UserProfilePutDto profilePutDto = new UserProfilePutDto("Lana", "Ilic", null, 160, 55, 23, Gender.FEMALE);

        UserProfileGetDto userProfileGetDtoExpected = userMapper.userToUserProfileGetDto(user);
        userProfileGetDtoExpected.setGender(Gender.FEMALE);
        userProfileGetDtoExpected.setAge(23);
        userProfileGetDtoExpected.setWeight(55);
        userProfileGetDtoExpected.setHeight(160);


        UserProfileGetDto userProfileGetDto = userService.updateWithProfile("lana.ilic99@gmail.com", profilePutDto);

        assertThat(userProfileGetDtoExpected).usingRecursiveComparison().isEqualTo(userProfileGetDto);
    }


    @Test
    void deleteByEmailShouldDeleteUser(){
        when(userRepository.existsByEmail("ilic.lana99@gmail.com")).thenReturn(true);

        userService.delete("ilic.lana99@gmail.com");

        verify(userRepository, times(1)).deleteByEmail("ilic.lana99@gmail.com");
    }

    @Test
    void willThrowWhenTryingToDeleteInitialAdmin(){
        when(initialAdminConfig.getEmail()).thenReturn("lana.ilic99@gmail.com");

        Assertions.assertThrows(AdminCannotBeModifiedException.class, () -> {
            userService.delete("lana.ilic99@gmail.com");
        });
    }

}