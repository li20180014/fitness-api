package rs.ac.bg.fon.FitnessPortal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.FitnessPortal.dtos.coach.CoachGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.coach.CoachPostDto;
import rs.ac.bg.fon.FitnessPortal.entities.Coach;
import rs.ac.bg.fon.FitnessPortal.exception_handling.EmailExistsException;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.CoachMapper;
import rs.ac.bg.fon.FitnessPortal.repositories.CoachRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.UserRepository;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;
import rs.ac.bg.fon.FitnessPortal.services.utility.UserConfigurer;

import java.util.List;

/**
 * Represents a service layer class responsible for implementing all Coach related API methods.
 * Available API method implementations: GET, POST
 *
 * @author Lana
 * @version 1.0
 */
@Service
public class CoachServiceImpl implements CoachService {

    /**
     * Instance of Coach repository class, responsible for interacting with Coach database table.
     */
    private CoachRepository coachRepository;

    /**
     * Instance of Mapstruct Mapper class, responsible for mapping from DTO to DAO and vice versa.
     */
    private CoachMapper coachMapper;
    /**
     * Instance of User repository class, responsible for interacting with User database table.
     */
    private UserRepository userRepository;

    /**
     * Instance of User Configurer class, responsible for handling user configurations such as encoding passwords and setting application roles.
     */
    private UserConfigurer userConfigurer;

    /**
     * Returns a list of coaches from database.
     *
     * @return List<CoachGetDto> list of instances of CoachGetDto class
     */
    @Override
    public List<CoachGetDto> get() {
        return coachMapper.coachesToCoachGetDtos(coachRepository.findAll());
    }

    /**
     * Responsible for adding new coach to database. Handles password encoding for new coach and setting new application roles.
     * Method returns coach after inserting.
     *
     * @param coachPostDto instance of CoachPostDto class with coach data to insert into database
     * @param roleTypes list of ApplicationUserRoles
     * @return CoachGetDto instance of Coach saved in database
     * @throws EmailExistsException if the coach with same email already exists in database
     */
    @Override
    @Transactional
    public CoachGetDto create(CoachPostDto coachPostDto, List<ApplicationUserRole> roleTypes) {
        if(userRepository.existsByEmail(coachPostDto.getEmail())) throw new EmailExistsException(coachPostDto.getEmail());

        Coach coach = coachMapper.coachPostDtoToCoach(coachPostDto);
        userConfigurer.addRoles(coach, roleTypes);
        userConfigurer.encodePassword(coach);
        coach.setEnabled(true);

        coachRepository.save(coach);
        return coachMapper.coachToCoachGetDto(coach);
    }

    /**
     * Sets coach repository to provided instance of CoachRepository class.
     *
     * @param coachRepository new Object instance of CoachRepository class
     */
    @Autowired
    public void setCoachRepository(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    /**
     * Sets coach mapper to provided instance of CoachMapper class.
     *
     * @param coachMapper new Object instance of mapstruct CoachMapper class
     */
    @Autowired
    public void setCoachMapper(CoachMapper coachMapper) {
        this.coachMapper = coachMapper;
    }

    /**
     * Sets user repository to provided instance of UserRepository class.
     *
     * @param userRepository new Object instance of UserRepository class
     */
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Sets user configurer to provided instance of UserConfigurer class.
     *
     * @param userConfigurer new Object instance of UserConfigurer class
     */
    @Autowired
    public void setUserConfigurer(UserConfigurer userConfigurer) {
        this.userConfigurer = userConfigurer;
    }
}
