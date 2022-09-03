package rs.ac.bg.fon.FitnessPortal.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.FitnessPortal.dtos.appointment.AppointmentAttendancePutDto;
import rs.ac.bg.fon.FitnessPortal.dtos.appointment.AppointmentWithMemberGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.appointment.AppointmentWithTrainingGetDto;
import rs.ac.bg.fon.FitnessPortal.entities.*;
import rs.ac.bg.fon.FitnessPortal.exception_handling.*;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.AppointmentMapper;
import rs.ac.bg.fon.FitnessPortal.mapstruct.mappers.AppointmentMapperImpl;
import rs.ac.bg.fon.FitnessPortal.repositories.AppointmentRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.MemberRepository;
import rs.ac.bg.fon.FitnessPortal.repositories.TrainingRepository;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Spy
    private AppointmentMapperImpl appointmentMapper;

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void getByMemberShouldThrowError() {
        when(memberRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            appointmentService.getByMember("lana.ilic99@gmail.com");
        });
    }

    @Test
    void getByMemberShouldReturnAppointments() {

        Member member = new Member();
        member.setEmail("lana.ilic99@gmail.com");
        Set<Appointment> appointments = new HashSet<>();
        appointments.add(new Appointment());
        member.setAppointments(appointments);
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        List<AppointmentWithTrainingGetDto> result = appointmentService.getByMember(member.getEmail());

        assertThat(result).usingRecursiveComparison().isEqualTo(member.getAppointments());

    }

    @Test
    void getByTrainingShouldThrowTrainingNotFound() {
        when(trainingRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(TrainingNotFoundException.class, () -> {
            appointmentService.getByTraining(1, "lana.ilic99@gmail.com");
        });
    }

    @Test
    void getByTrainingShouldThrowInvalidUser() {
        Training training = new Training();
        training.setId(1);
        Coach coach = new Coach();
        coach.setEmail("lana.ilic99@gmail.com");
        training.setCoach(coach);

        when(trainingRepository.findById(training.getId())).thenReturn(Optional.of(training));

        Assertions.assertThrows(InvalidUserException.class, () -> {
            appointmentService.getByTraining(1, "lana99@gmail.com");
        });
    }

    @Test
    void getByTrainingShouldReturnAppointments() {
        Training training = new Training();
        training.setId(1);
        Coach coach = new Coach();
        coach.setEmail("lana.ilic99@gmail.com");
        training.setCoach(coach);

        Set<Appointment> appointments = new HashSet<>();
        appointments.add(new Appointment());
        training.setAppointments(appointments);

        when(trainingRepository.findById(training.getId())).thenReturn(Optional.of(training));

        List<AppointmentWithMemberGetDto> result = appointmentService.getByTraining(1, "lana.ilic99@gmail.com");
        assertThat(result).usingRecursiveComparison().isEqualTo(training.getAppointments());

    }

    @Test
    void scheduleAppointmentShouldThrowUserNotFound() {
        when(memberRepository.findByEmail("lana.ilic99@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            appointmentService.scheduleAppointment(1, "lana.ilic99@gmail.com");
        });
    }

    @Test
    void scheduleAppointmentShouldThrowTrainingNotFound() {
        Member member = new Member();
        member.setEmail("lana.ilic99@gmail.com");

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        when(trainingRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(TrainingNotFoundException.class, () -> {
            appointmentService.scheduleAppointment(1, "lana.ilic99@gmail.com");
        });
    }

    @Test
    void scheduleAppointmentShouldThrowInvalidNumberOfSpots() throws InvalidNumberOfSpotsException {
        Member member = new Member();
        member.setEmail("lana.ilic99@gmail.com");

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        Training training = new Training();
        training.setId(1);
        training.setRemainingSpots(0);

        when(trainingRepository.findById(training.getId())).thenReturn(Optional.of(training));

        Exception exception = Assertions.assertThrows(InvalidNumberOfSpotsException.class, () -> {
            appointmentService.scheduleAppointment(1, "lana.ilic99@gmail.com");
        });

        String expectedMessage = "There are no remaining spots on this training, they are all already taken";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void scheduleAppointmentShouldThrowAlreadyScheduled() throws InvalidNumberOfSpotsException {
        Member member = new Member();
        member.setEmail("lana.ilic99@gmail.com");

        Training training = new Training();
        training.setId(1);
        training.setRemainingSpots(5);
        training.setDate(LocalDate.of(2022,9,4));

        Set<Appointment> appointments = new HashSet<>();
        Appointment a = new Appointment();
        a.setTraining(training);
        appointments.add(a);

        member.setAppointments(appointments);

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));
        when(trainingRepository.findById(training.getId())).thenReturn(Optional.of(training));

        Exception exception = Assertions.assertThrows(AlreadyScheduledException.class, () -> {
            appointmentService.scheduleAppointment(1, "lana.ilic99@gmail.com");
        });

        String expectedMessage = "You have already scheduled a training appointment on that date";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void scheduleAppointmentShouldSchedule() throws InvalidNumberOfSpotsException {
        Member member = new Member();
        member.setEmail("lana.ilic99@gmail.com");

        Training training2 = new Training();
        training2.setId(2);
        training2.setRemainingSpots(6);
        training2.setDate(LocalDate.of(2022,10,4));

        Set<Appointment> appointments = new HashSet<>();
        Appointment a = new Appointment();
        a.setTraining(training2);
        appointments.add(a);

        member.setAppointments(appointments);

        Training training = new Training();
        training.setId(1);
        training.setRemainingSpots(5);
        training.setDate(LocalDate.of(2022,9,4));

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));
        when(trainingRepository.findById(training.getId())).thenReturn(Optional.of(training));

        Appointment appointment = new Appointment(member, training);

        appointmentService.scheduleAppointment(1, "lana.ilic99@gmail.com");

        ArgumentCaptor<Appointment> appointmentArgumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(appointmentArgumentCaptor.capture());

        Appointment capturedAppointment = appointmentArgumentCaptor.getValue();

        assertThat(capturedAppointment).usingRecursiveComparison().isEqualTo(appointment);

    }

    @Test
    void updateAttendanceShouldThrowAppointmentNotFound() {

        when(appointmentRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.updateAttendance(1, new AppointmentAttendancePutDto(), "lana.ilic99@gmail.com");
        });
    }

    @Test
    void updateAttendanceShouldThrowInvalidUserException() {

        Appointment a = new Appointment();
        a.setId(1);
        Training t = new Training();
        Coach c= new Coach();
        c.setEmail("lana@gmail.com");
        t.setCoach(c);
        a.setTraining(t);

        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));

        Assertions.assertThrows(InvalidUserException.class, () -> {
            appointmentService.updateAttendance(1, new AppointmentAttendancePutDto(), "lana.ilic99@gmail.com");
        });
    }

    @Test
    void updateAttendanceShouldThrowFutureAppointmentException() {

        Appointment a = new Appointment();
        a.setId(1);
        Training t = new Training();
        t.setDate(LocalDate.of(2022,9,4));
        t.setStartTime(LocalTime.of(16,0,0));
        Coach c= new Coach();
        c.setEmail("lana.ilic99@gmail.com");
        t.setCoach(c);
        a.setTraining(t);

        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));

        Assertions.assertThrows(FutureAppointmentException.class, () -> {
            appointmentService.updateAttendance(1, new AppointmentAttendancePutDto(), "lana.ilic99@gmail.com");
        });
    }

    @Test
    void updateAttendanceShouldUpdate() {

        Appointment a = new Appointment();
        a.setId(1);
        Training t = new Training();
        t.setDate(LocalDate.of(2022,9,2));
        t.setStartTime(LocalTime.of(14,0,0));
        Coach c= new Coach();
        c.setEmail("lana.ilic99@gmail.com");
        t.setCoach(c);
        a.setTraining(t);

        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));

        AppointmentAttendancePutDto dto = new AppointmentAttendancePutDto();
        dto.setAttended(true);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(a);

        AppointmentWithMemberGetDto appointmentWithMemberGetDto = appointmentService.updateAttendance(1, dto, "lana.ilic99@gmail.com");

        a.setAttended(true);
        assertThat(appointmentWithMemberGetDto).usingRecursiveComparison().isEqualTo(a);

    }
}