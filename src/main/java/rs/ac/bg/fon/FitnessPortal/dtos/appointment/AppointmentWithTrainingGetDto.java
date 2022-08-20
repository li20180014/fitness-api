package rs.ac.bg.fon.FitnessPortal.dtos.appointment;

import rs.ac.bg.fon.FitnessPortal.dtos.training.TrainingGetDto;

public class AppointmentWithTrainingGetDto {

    private Integer id;
    private Boolean attended;
    private TrainingGetDto training;

    public AppointmentWithTrainingGetDto() { }

    public AppointmentWithTrainingGetDto(Integer id, Boolean attended, TrainingGetDto training) {
        this.id = id;
        this.attended = attended;
        this.training = training;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public TrainingGetDto getTraining() {
        return training;
    }

    public void setTraining(TrainingGetDto training) {
        this.training = training;
    }
}
