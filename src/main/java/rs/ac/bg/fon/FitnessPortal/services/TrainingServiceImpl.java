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

@Service
public class TrainingServiceImpl implements TrainingService {

    private CoachRepository coachRepository;
    private MemberRepository memberRepository;
    private TrainingRepository trainingRepository;
    private TrainingMapper trainingMapper;

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

    @Override
    public List<TrainingWithoutCoachGetDto> getByCoach(String coachEmail) {
        Coach coach = coachRepository.findByEmail(coachEmail).orElseThrow(() -> new UserNotFoundException(coachEmail));

        List<Training> trainings = new ArrayList<>(coach.getTrainings());

        return trainingMapper.trainingsToTrainingWithoutCoachGetDtos(trainings);
    }

    private List<Training> getAvailableTrainings(List<Training> allTrainings) {
        List<Training> availableTrainings = new ArrayList<>();

        for (Training t: allTrainings) {
            if(t.getRemainingSpots() != 0)
                availableTrainings.add(t);
        }
        return availableTrainings;
    }

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

    private boolean isTakenInterval(TrainingPostDto trainingPostDto, Coach coach) {
        List<Training> trainings = trainingRepository.findAllByDateAndCoach(trainingPostDto.getDate(), coach);

        for (Training t: trainings) {
            if(isOverlapping(t.getStartTime(), t.getEndTime(), trainingPostDto.getStartTime(), trainingPostDto.getEndTime()))
                return true;
        }
        return false;
    }

    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2){
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    @Autowired
    public void setCoachRepository(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    @Autowired
    public void setTrainingRepository(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Autowired
    public void setTrainingMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
