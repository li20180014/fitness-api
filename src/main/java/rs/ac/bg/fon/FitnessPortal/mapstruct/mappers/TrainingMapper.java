package rs.ac.bg.fon.FitnessPortal.mapstruct.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingPostDto;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingWithoutCoachGetDto;
import rs.ac.bg.fon.FitnessPortal.entities.Training;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TrainingMapper {
    TrainingGetDto trainingToTrainingGetDto(Training training);
    Training trainingPostDtoToTraining(TrainingPostDto trainingPostDto);
    List<TrainingGetDto> trainingsToTrainingGetDtos(List<Training> trainings);
    TrainingWithoutCoachGetDto trainingToTrainingWithoutCoachGetDto(Training training);
    List<TrainingWithoutCoachGetDto> trainingsToTrainingWithoutCoachGetDtos(List<Training> trainings);
}
