package rs.ac.bg.fon.FitnessPortal.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.FitnessPortal.dtos.coach.CoachGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.coach.CoachPostDto;
import rs.ac.bg.fon.FitnessPortal.dtos.user.UserPostDto;
import rs.ac.bg.fon.FitnessPortal.entities.Coach;
import rs.ac.bg.fon.FitnessPortal.entities.Role;
import rs.ac.bg.fon.FitnessPortal.entities.User;
import rs.ac.bg.fon.FitnessPortal.exception_handling.EmailExistsException;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.CoachMapperImpl;
import rs.ac.bg.fon.FitnessPortal.repositories.CoachRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.UserRepository;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;
import rs.ac.bg.fon.FitnessPortal.services.utility.UserConfigurer;

import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachServiceImplTest {

    @Mock
    private CoachRepository coachRepository;

    @Spy
    private CoachMapperImpl coachMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConfigurer userConfigurer;
    @InjectMocks
    private CoachServiceImpl coachService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllShouldReturnAllCoaches() {
        List<Coach> predefinedList = new ArrayList<>();

       Coach coach1 = new Coach(1, "Demo", "Demo", "Demo", "Demo", 10, "Demo", "Demo");
        Coach coach2 = new Coach(2, "Demo", "Demo", "Demo", "Demo", 10, "Demo", "Demo");

        predefinedList.add(coach1);
        predefinedList.add(coach2);

        when(coachRepository.findAll()).thenReturn(predefinedList);

        List<CoachGetDto> fetchedList = coachService.get();

        assertThat(fetchedList).usingRecursiveComparison().isEqualTo(predefinedList);
    }

    @Test
    void createShouldThrowEmailExists() {

        CoachPostDto coachPostDto = new CoachPostDto();
        coachPostDto.setEmail("lana.ilic99@gmail.com");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        Assertions.assertThrows(EmailExistsException.class, () -> {
            coachService.create(coachPostDto, null);
        });
    }

    @Test
    void createShouldCreateBlog() {

        CoachPostDto coachPostDto = new CoachPostDto();
        coachPostDto.setEmail("lana.ilic99@gmail.com");
        coachPostDto.setId(1);
        coachPostDto.setBiography("Demo");
        coachPostDto.setPassword("jsnf");
        coachPostDto.setYearsOfExperience(10);
        coachPostDto.setImageSrc("www.linkedin.com");


        List<ApplicationUserRole> roleTypes = List.of(ApplicationUserRole.COACH);

        doAnswer(invocation -> {
            Coach coach = (Coach) invocation.getArgument(0);
            List<ApplicationUserRole> roleTypes1 =(List<ApplicationUserRole>) invocation.getArgument(1);

            Set<Role> roles = createRoles(roleTypes1);

            coach.setRoles(roles);

            return null;
        }).when(userConfigurer).addRoles(any(Coach.class), any(List.class));

        doAnswer(invocation -> {
            Coach coach = (Coach)  invocation.getArgument(0);
            coach.setPassword("123");
            return null;
        }).when(userConfigurer).encodePassword(any(Coach.class));

        coachService.create(coachPostDto, roleTypes);

        ArgumentCaptor<Coach> coachArgumentCaptor = ArgumentCaptor.forClass(Coach.class);
        verify(coachRepository).save(coachArgumentCaptor.capture());

        Coach capturedCoach = coachArgumentCaptor.getValue();

        Coach coachWithRole = coachMapper.coachPostDtoToCoach(coachPostDto);
        coachWithRole.addRole(new Role(ApplicationUserRole.COACH));
        coachWithRole.setPassword("123");
        coachWithRole.setEnabled(true);

        assertThat(coachWithRole).usingRecursiveComparison().isEqualTo(capturedCoach);

    }

    private Set<Role> createRoles(List<ApplicationUserRole> roleTypes1) {
        Set<Role> roles = new HashSet<>();

        for (ApplicationUserRole rt: roleTypes1) {
            roles.add(new Role(rt));
        }
        return roles;
    }
}