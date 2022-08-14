package rs.ac.bg.fon.FitnessPortal.mapstruct.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.FitnessPortal.dtos.coach.CoachGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.coach.CoachPostDto;
import rs.ac.bg.fon.FitnessPortal.entities.Coach;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CoachMapper {
    CoachGetDto coachToCoachGetDto(Coach coach);
    Coach coachPostDtoToCoach(CoachPostDto coachPostDto);
    List<CoachGetDto> coachesToCoachGetDtos(List<Coach> coaches);
}
