package rs.ac.bg.fon.FitnessPortal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.FitnessPortal.dtos.appointment.AppointmentAttendancePutDto;
import rs.ac.bg.fon.FitnessPortal.dtos.appointment.AppointmentWithMemberGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.appointment.AppointmentWithTrainingGetDto;
import rs.ac.bg.fon.FitnessPortal.entities.Appointment;
import rs.ac.bg.fon.FitnessPortal.entities.Member;
import rs.ac.bg.fon.FitnessPortal.entities.Training;
import rs.ac.bg.fon.FitnessPortal.exception_handling.*;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.AppointmentMapper;
import rs.ac.bg.fon.FitnessPortal.repositories.AppointmentRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.MemberRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.TrainingRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a service layer class responsible for implementing all Appointment related API methods.
 * Available API method implementations: GET, POST, PUT
 *
 * @author Lana
 * @version 1.0
 */
@Service
public class AppointmentServiceImpl implements AppointmentService{

    /**
     * Instance of Member repository class, responsible for interacting with Member database table.
     */
    private MemberRepository memberRepository;
    /**
     * Instance of Appointment repository class, responsible for interacting with Appointment database table.
     */
    private AppointmentRepository appointmentRepository;
    /**
     * Instance of Mapstruct Mapper class, responsible for mapping from DTO to DAO and vice versa.
     */
    private AppointmentMapper appointmentMapper;
    /**
     * Instance of Training repository class, responsible for interacting with Training database table.
     */
    private TrainingRepository trainingRepository;


    /**
     * Returns list of appointments scheduled by provided Member.
     * If member exist in database returns his list of appointments, if he doesn't throws an exception.
     *
     * @param memberEmail string value representing members email address
     * @return List<AppointmentWithTrainingGetDto> list of appointments with trainings
     * @throws UserNotFoundException if member by passed email doesn't exist
     */
    @Override
    public List<AppointmentWithTrainingGetDto> getByMember(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(() -> new UserNotFoundException(memberEmail));

        List<Appointment> appointments = new ArrayList<>(member.getAppointments());

        return appointmentMapper.appointmentsToAppointmentWithTrainingGetDtos(appointments);
    }

    /**
     * Returns a list of appointments for a specific training.
     * If training exists in database returns a list of appointments, if not throws an exception.
     * If coach's email requesting appointment data does not match coach who is in charge of training, throws an exception.
     *
     * @param trainingID integer value of Training id number
     * @param coachEmail string value of coach's email adress
     * @return List<AppointmentWithMemberGetDto> list of appointments with its members
     * @throws TrainingNotFoundException if training by provided id does't exist in database
     * @throws InvalidUserException if provided user email does not match user in charge of training
     */
    @Override
    public List<AppointmentWithMemberGetDto> getByTraining(Integer trainingID, String coachEmail) {
        Training training = trainingRepository.findById(trainingID).orElseThrow(() -> new TrainingNotFoundException(trainingID));

        if(!training.getCoach().getEmail().equals(coachEmail)) throw new InvalidUserException();

        List<Appointment> appointments = new ArrayList<>(training.getAppointments());

        return appointmentMapper.appointmentsToAppointmentWithMemberGetDtos(appointments);
    }

    /**
     * Schedules new appointment. The new appointment instance has a reference to which training is being
     * scheduled and by which member.
     *
     * @param trainingID integer value of training id number
     * @param userEmail string value of member email adress
     * @throws TrainingNotFoundException if training by provided Id doesnt exist in database
     * @throws InvalidUserException if user by provided email is not in charge of training
     * @throws AlreadyScheduledException if the provided user has already scheduled that training
     * @throws InvalidNumberOfSpotsException if the requested training for scheduling has no remaining spots left
     */
    @Override
    @Transactional
    public void scheduleAppointment(Integer trainingID, String userEmail) {
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));

        Training training = trainingRepository.findById(trainingID).orElseThrow(() -> new TrainingNotFoundException(trainingID));

        if(training.getRemainingSpots() == 0)
            throw new InvalidNumberOfSpotsException("There are no remaining spots on this training, they are all already taken");

        if(alreadyScheduledOnThatDate(training, member)) throw new AlreadyScheduledException();

        training.setRemainingSpots(training.getRemainingSpots() - 1);

        Appointment appointment = new Appointment(member, training);

        appointmentRepository.save(appointment);
    }

    /**
     * Updates member attendance on training. Sets weather a member has attended a training or not.
     *
     * @param appointmentID integer value of appointment id number
     * @param appointmentAttendancePutDto object of class AppointmentAttendancePutDto with attendance data
     * @param coachEmail string value of coach's email address
     * @return AppointmentWithMemberGetDto object of class AppointmentWithMemberGetDto with new attendance data
     * @throws AppointmentNotFoundException if the appointment does not exist in database
     * @throws InvalidUserException if coach who is trying to update member attendance is not in charge of the training
     * @throws FutureAppointmentException if training start date is after current date
     */
    @Override
    @Transactional
    public AppointmentWithMemberGetDto updateAttendance(Integer appointmentID, AppointmentAttendancePutDto appointmentAttendancePutDto, String coachEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentID).orElseThrow(() -> new AppointmentNotFoundException(appointmentID));

        if(!appointment.getTraining().getCoach().getEmail().equals(coachEmail)) throw new InvalidUserException();

        if(isAfterCurrentDateTime(appointment)) throw new FutureAppointmentException();

        appointment.setAttended(appointmentAttendancePutDto.getAttended());

        return appointmentMapper.appointmentToAppointmentWithMemberGetDto(appointmentRepository.save(appointment));
    }

    /**
     * Checks if appointment is after current date. If appointment is after current date returns true, otherwise returns false.
     *
     * @param appointment object of class Appointment
     * @return boolean value
     */
    private boolean isAfterCurrentDateTime(Appointment appointment) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime appointmentStartDateTime = LocalDateTime.of(appointment.getTraining().getDate(), appointment.getTraining().getStartTime());

        return appointmentStartDateTime.isAfter(currentDateTime);
    }

    /**
     * Checks if member has already scheduled a training. Returns false if member hasn't already scheduled that training
     * otherwise returns true.
     *
     * @param training object of class Training
     * @param member object of class Member
     * @return boolean value
     */

    private boolean alreadyScheduledOnThatDate(Training training, Member member) {
        for(Appointment appointment: member.getAppointments()){
            if(appointment.getTraining().getDate().equals(training.getDate())) return true;
        }
        return false;
    }

    /**
     * Sets member repository to provided instance of MemberRepository class.
     *
     * @param memberRepository new Object instance of MemberRepository class
     */
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Sets appointment repository to provided instance of AppointmentRepository class.
     *
     * @param appointmentRepository new Object instance of AppointmentRepository class
     */
    @Autowired
    public void setAppointmentRepository(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Sets appointment mapper to provided instance of AppointmentMapper class.
     *
     * @param appointmentMapper new Object instance of AppointmentMapper class
     */
    @Autowired
    public void setAppointmentMapper(AppointmentMapper appointmentMapper) {
        this.appointmentMapper = appointmentMapper;
    }

    /**
     * Sets training repository to provided instance of TrainingRepository class.
     *
     * @param trainingRepository new Object instance of TrainingRepository class
     */
    @Autowired
    public void setTrainingRepository(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }
}

