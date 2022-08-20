package rs.ac.bg.fon.FitnessPortal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.FitnessPortal.entities.Coach;
import rs.ac.bg.fon.FitnessPortal.entities.Training;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
    List<Training> findAllByDateAndCoach(LocalDate date, Coach coach);
    List<Training> findAllByCoach(Coach coach);
}
