package rs.ac.bg.fon.FitnessPortal.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingPostDto;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingWithoutCoachGetDto;
import rs.ac.bg.fon.FitnessPortal.entities.Coach;
import rs.ac.bg.fon.FitnessPortal.entities.Training;
import rs.ac.bg.fon.FitnessPortal.exception_handling.*;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.TrainingMapper;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.TrainingMapperImpl;
import rs.ac.bg.fon.FitnessPortal.repositories.CoachRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.MemberRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.TrainingRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private CoachRepository coachRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Spy
    private TrainingMapperImpl trainingMapper;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    TrainingServiceImplTest() {
    }

    @Test
    void getAvailableByCoachIdShouldThrowError() {
        when(coachRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            trainingService.getAvailableByCoachID(1);
        });
    }

    @Test
    void getAvailableByCoachIdShouldReturnTrainings() {
        Coach coach = new Coach();
        coach.setId(1);
        when(coachRepository.findById(1)).thenReturn(Optional.of(coach));

        List<Training> trainings = new ArrayList<>();
        Training t = new Training();
        t.setRemainingSpots(5);
        t.setCoach(coach);
        trainings.add(t);

        when(trainingRepository.findAllByCoach(coach)).thenReturn(trainings);

        List<TrainingGetDto> availableTrainings = trainingService.getAvailableByCoachID(1);

        assertThat(availableTrainings).usingRecursiveComparison().isEqualTo(trainings);
    }

    @Test
    void getAvailableByCoachIdShouldReturnEmptyList() {
        Coach coach = new Coach();
        coach.setId(1);
        when(coachRepository.findById(1)).thenReturn(Optional.of(coach));

        List<Training> trainings = new ArrayList<>();
        Training t = new Training();
        t.setRemainingSpots(0);
        t.setCoach(coach);
        trainings.add(t);

        when(trainingRepository.findAllByCoach(coach)).thenReturn(trainings);

        List<TrainingGetDto> availableTrainings = trainingService.getAvailableByCoachID(1);

        assertThat(availableTrainings.size()).isEqualTo(0);
    }

    @Test
    void createShouldThrowErrorUserNotFound() {
        when(coachRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            trainingService.create(new TrainingPostDto(),"lana.ilic99@gmail.com" );
        });
    }

    @Test
    void createShouldCreateNewTraining()  {
        Coach coach = new Coach();
        coach.setId(1);
        coach.setEmail("lana.ilic99@gmail.com");
        when(coachRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.of(coach));

        TrainingPostDto trainingPostDto = new TrainingPostDto();
        trainingPostDto.setDate( LocalDate.of(2022, 9, 4));
        trainingPostDto.setStartTime(LocalTime.of(15,0,0));
        trainingPostDto.setEndTime(LocalTime.of(16,0,0));
        trainingPostDto.setMaxSpots(5);


        Training t = new Training();
        t.setDate( LocalDate.of(2022, 9, 4));
        t.setStartTime(LocalTime.of(17,0,0));
        t.setEndTime(LocalTime.of(18,0,0));
        when(trainingRepository.findAllByDateAndCoach(trainingPostDto.getDate(), coach)).thenReturn(List.of(t));

        Training training = trainingMapper.trainingPostDtoToTraining(trainingPostDto);
        training.setCoach(coach);
        training.setRemainingSpots(trainingPostDto.getMaxSpots());


        when(trainingRepository.save(any(Training.class))).thenReturn(training);

        TrainingGetDto trainingGetDto = trainingService.create(trainingPostDto, coach.getEmail());

        assertThat(trainingGetDto).usingRecursiveComparison().isEqualTo(training);

    }


    @Test
    void createShouldThrowInvalidNumberOfSpots() throws InvalidNumberOfSpotsException {
        Coach coach = new Coach();
        coach.setId(1);
        coach.setEmail("lana.ilic99@gmail.com");
        when(coachRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.of(coach));

        TrainingPostDto trainingPostDto = new TrainingPostDto();
        trainingPostDto.setDate( LocalDate.of(2022, 9, 4));
        trainingPostDto.setStartTime(LocalTime.of(15,0,0));
        trainingPostDto.setEndTime(LocalTime.of(16,0,0));

        Training t = new Training();
        t.setDate( LocalDate.of(2022, 9, 4));
        t.setStartTime(LocalTime.of(17,0,0));
        t.setEndTime(LocalTime.of(18,0,0));
        when(trainingRepository.findAllByDateAndCoach(trainingPostDto.getDate(), coach)).thenReturn(List.of(t));


        trainingPostDto.setMaxSpots(0);

        Exception exception = Assertions.assertThrows(InvalidNumberOfSpotsException.class, () -> {
            trainingService.create(trainingPostDto, "lana.ilic99@gmail.com");
        });

        String expectedMessage = "Maximum number of spots has to be a positive integer";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void createShouldThrowInvalidTrainingTime() throws InvalidTrainingTimeException {
        Coach coach = new Coach();
        coach.setId(1);
        coach.setEmail("lana.ilic99@gmail.com");
        when(coachRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.of(coach));

        TrainingPostDto trainingPostDto = new TrainingPostDto();
        trainingPostDto.setDate( LocalDate.of(2022, 9, 4));
        trainingPostDto.setStartTime(LocalTime.of(15,0,0));
        trainingPostDto.setEndTime(LocalTime.of(16,0,0));

        Training t = new Training();
        t.setDate( LocalDate.of(2022, 9, 4));
        t.setStartTime(LocalTime.of(15,0,0));
        t.setEndTime(LocalTime.of(16,0,0));
        when(trainingRepository.findAllByDateAndCoach(trainingPostDto.getDate(), coach)).thenReturn(List.of(t));


        Exception exception = Assertions.assertThrows(InvalidTrainingTimeException.class, () -> {
            trainingService.create(trainingPostDto, "lana.ilic99@gmail.com");
        });

        String expectedMessage = "The training in this time interval is already scheduled";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void createShouldThrowInvalidTrainingTimeMultipleMessages() throws InvalidTrainingTimeException {
        Coach coach = new Coach();
        coach.setId(1);
        coach.setEmail("lana.ilic99@gmail.com");
        when(coachRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.of(coach));

        TrainingPostDto trainingPostDto = new TrainingPostDto();
        trainingPostDto.setDate( LocalDate.of(2022, 8, 4));
        trainingPostDto.setStartTime(LocalTime.of(15,0,0));
        trainingPostDto.setEndTime(LocalTime.of(15,20,0));

        Training t = new Training();
        t.setDate( LocalDate.of(2022, 8, 4));
        t.setStartTime(LocalTime.of(15,0,0));
        t.setEndTime(LocalTime.of(16,0,0));
        when(trainingRepository.findAllByDateAndCoach(trainingPostDto.getDate(), coach)).thenReturn(List.of(t));


        Exception exception = Assertions.assertThrows(InvalidTrainingTimeException.class, () -> {
            trainingService.create(trainingPostDto, "lana.ilic99@gmail.com");
        });

        String expectedMessage = "Start time must be at least 30 minutes before the end time\n";
//        expectedMessage += "Maximum training length is 2 hours\n";
        expectedMessage += "Training date must be after the current date\n";
        expectedMessage += "The training in this time interval is already scheduled";


        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void createShouldThrowInvalidTrainingTimeTrainingTooLong() throws InvalidTrainingTimeException {
        Coach coach = new Coach();
        coach.setId(1);
        coach.setEmail("lana.ilic99@gmail.com");
        when(coachRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.of(coach));

        TrainingPostDto trainingPostDto = new TrainingPostDto();
        trainingPostDto.setDate( LocalDate.of(2022, 9, 4));
        trainingPostDto.setStartTime(LocalTime.of(15,0,0));
        trainingPostDto.setEndTime(LocalTime.of(18,0,0));

        Training t = new Training();
        t.setDate( LocalDate.of(2022, 9, 4));
        t.setStartTime(LocalTime.of(20,0,0));
        t.setEndTime(LocalTime.of(21,0,0));
        when(trainingRepository.findAllByDateAndCoach(trainingPostDto.getDate(), coach)).thenReturn(List.of(t));


        Exception exception = Assertions.assertThrows(InvalidTrainingTimeException.class, () -> {
            trainingService.create(trainingPostDto, "lana.ilic99@gmail.com");
        });

        String expectedMessage = "Maximum training length is 2 hours";

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void getByCoachShouldThrowError() {
        when(coachRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            trainingService.getByCoach("lana.ilic99@gmail.com" );
        });
    }

    @Test
    void getByCoachShouldReturnTrainings() {
        Coach coach = new Coach();
        coach.setId(1);
        coach.setEmail("lana.ilic99@gmail.com");

        Set<Training> trainings = new HashSet<>();
        Training t = new Training();
        t.setRemainingSpots(0);
        t.setCoach(coach);
        trainings.add(t);

        coach.setTrainings(trainings);
        when(coachRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.of(coach));


        List<TrainingWithoutCoachGetDto> actualTrainings = trainingService.getByCoach(coach.getEmail());

        assertThat(actualTrainings).usingRecursiveComparison().isEqualTo(coach.getTrainings());

    }
}