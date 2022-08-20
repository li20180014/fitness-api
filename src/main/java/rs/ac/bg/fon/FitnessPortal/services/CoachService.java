package rs.ac.bg.fon.FitnessPortal.services;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.FitnessPortal.dtos.coach.CoachGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.coach.CoachPostDto;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;

import java.util.List;

@Service
public interface CoachService {

    List<CoachGetDto> get();
    CoachGetDto create(CoachPostDto coachPostDto, List<ApplicationUserRole> roleTypes);
}
