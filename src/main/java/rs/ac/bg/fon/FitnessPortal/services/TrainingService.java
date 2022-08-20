package rs.ac.bg.fon.FitnessPortal.services;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingPostDto;
import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingWithoutCoachGetDto;

import java.util.List;

@Service
public interface TrainingService {
    TrainingGetDto create(TrainingPostDto trainingPostDto, String userEmail);
    List<TrainingGetDto> getAvailableByCoachID(Integer coachID);
    List<TrainingWithoutCoachGetDto> getByCoach(String coachEmail);
}
