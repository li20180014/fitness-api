package rs.ac.bg.fon.FitnessPortal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingPostDto;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingWithoutCoachGetDto;
import rs.ac.bg.fon.FitnessPortal.entities.Coach;
import rs.ac.bg.fon.FitnessPortal.entities.Training;
import rs.ac.bg.fon.FitnessPortal.exception_handling.*;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.TrainingMapper;
import rs.ac.bg.fon.FitnessPortal.repositories.CoachRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.MemberRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.TrainingRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a service layer class responsible for implementing all Training related API methods.
 * Available API method implementations: GET, POST
 *
 * @author Lana
 * @version 1.0
 */
@Service
public class TrainingServiceImpl implements TrainingService {

    /**
     * Instance of Coach repository class, responsible for interacting with Coach database table.
     */
    private CoachRepository coachRepository;
    /**
     * Instance of Member repository class, responsible for interacting with Member database table.
     */
    private MemberRepository memberRepository;
    /**
     * Instance of Training repository class, responsible for interacting with Training database table.
     */
    private TrainingRepository trainingRepository;
    /**
     * Instance of Mapstruct Mapper class, responsible for mapping from DTO to DAO and vice versa.
     */
    private TrainingMapper trainingMapper;

    /**
     * Returns a list of available trainings by coach from database.
     *
     * @param coachID integer value of coach's id number.
     * @return List<TrainingGetDto> list of instances of TrainingGetDto's representing available trainings in database.
     * @throws UserNotFoundException if coach with provided id doesn't exist in database.
     */
    @Override
    public List<TrainingGetDto> getAvailableByCoachID(Integer coachID) {
        Coach coach = coachRepository.findById(coachID).orElseThrow(() -> new UserNotFoundException(coachID));

        List<Training> allTrainings = trainingRepository.findAllByCoach(coach);

        List<Training> availableTrainings = getAvailableTrainings(allTrainings);

        return trainingMapper.trainingsToTrainingGetDtos(availableTrainings);
    }


    @Override
    @Transactional
    public TrainingGetDto create(TrainingPostDto trainingPostDto, String userEmail) {
        Coach coach = coachRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));

        validateTrainingDateTime(trainingPostDto, coach);

        if(trainingPostDto.getMaxSpots() <= 0)
            throw new InvalidNumberOfSpotsException("Maximum number of spots has to be a positive integer");

        Training training = trainingMapper.trainingPostDtoToTraining(trainingPostDto);
        training.setRemainingSpots(trainingPostDto.getMaxSpots());
        training.setCoach(coach);

        return trainingMapper.trainingToTrainingGetDto(trainingRepository.save(training));
    }

    /**
     * Returns a list of all trainings by coach from database.
     *
     * @param coachEmail string value of coach's email address..
     * @return List<TrainingWithoutCoachGetDto> list of instances of TrainingWithoutCoachGetDto's representing all trainings by coach in database.
     * @throws UserNotFoundException if coach with provided email doesn't exist in database.
     */
    @Override
    public List<TrainingWithoutCoachGetDto> getByCoach(String coachEmail) {
        Coach coach = coachRepository.findByEmail(coachEmail).orElseThrow(() -> new UserNotFoundException(coachEmail));

        List<Training> trainings = new ArrayList<>(coach.getTrainings());

        return trainingMapper.trainingsToTrainingWithoutCoachGetDtos(trainings);
    }

    /**
     * Returns list of all available trainings. Trainings are available if their number of remaining spots is greater than zero.
     *
     * @param allTrainings list of instances of class Training
     * @return List<Training> list of instances of class Training representing available trainings
     */
    private List<Training> getAvailableTrainings(List<Training> allTrainings) {
        List<Training> availableTrainings = new ArrayList<>();

        for (Training t: allTrainings) {
            if(t.getRemainingSpots() != 0)
                availableTrainings.add(t);
        }
        return availableTrainings;
    }

    /**
     * Method responsible for validating training data. Throws error with string of concatenated error messages
     * if any of the conditions haven't passed.
     *
     * @param trainingPostDto instance of class TrainingPostDto with training data.
     * @param coach instance of class Coach
     * @throws InvalidTrainingTimeException if any of the Training data haven't passed validation.
     */
    private void validateTrainingDateTime(TrainingPostDto trainingPostDto, Coach coach){
        String message = "";

        if(trainingPostDto.getEndTime().minusMinutes(30).isBefore(trainingPostDto.getStartTime()))
            message += "Start time must be at least 30 minutes before the end time\n";

        if(trainingPostDto.getStartTime().plusMinutes(120).isBefore(trainingPostDto.getEndTime()))
            message += "Maximum training length is 2 hours\n";

        if(trainingPostDto.getDate().isBefore(LocalDate.now()))
            message += "Training date must be after the current date\n";

        if(isTakenInterval(trainingPostDto, coach))
            message += "The training in this time interval is already scheduled";

        if(!message.isEmpty()) throw new InvalidTrainingTimeException(message);
    }

    /**
     * Checks if a training in passed interval already exists in database.
     *
     * @param trainingPostDto instance of class TrainingPostDto with training data.
     * @param coach instance of class Coach
     * @return boolean value, returns true if a training with same start time exists in database,
     * otherwise returns false.
     */
    private boolean isTakenInterval(TrainingPostDto trainingPostDto, Coach coach) {
        List<Training> trainings = trainingRepository.findAllByDateAndCoach(trainingPostDto.getDate(), coach);

        for (Training t: trainings) {
            if(isOverlapping(t.getStartTime(), t.getEndTime(), trainingPostDto.getStartTime(), trainingPostDto.getEndTime()))
                return true;
        }
        return false;
    }

    /**
     * Checks if two provided dates are overlapping.
     *
     * @param start1 instance of LocalTime representing the start time of the first training.
     * @param end1 instance of LocalTime representing the end time of the first training.
     * @param start2 instance of LocalTime representing the start time of the second training.
     * @param end2 instance of LocalTime representing the end time of the second training.
     * @return boolean value, returns true if one date is before the other, otherwise returns false.
     */
    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2){
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /**
     * Sets coach repository to provided instance of CoachRepository class.
     *
     * @param coachRepository new Object instance of CoachRepository class.
     */
    @Autowired
    public void setCoachRepository(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    /**
     * Sets training repository to provided instance of TrainingRepository class.
     *
     * @param trainingRepository new Object instance of TrainingRepository class.
     */
    @Autowired
    public void setTrainingRepository(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    /**
     * Sets training mapper to provided instance of TrainingMapper class.
     *
     * @param trainingMapper new Object instance of mapstruct TrainingMapper class.
     */
    @Autowired
    public void setTrainingMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
    }

    /**
     * Sets member repository to provided instance of MemberRepository class.
     *
     * @param memberRepository new Object instance of MemberRepository class.
     */
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
