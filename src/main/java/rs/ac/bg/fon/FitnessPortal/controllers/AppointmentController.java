package rs.ac.bg.fon.FitnessPortal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.FitnessPortal.dtos.appointment.AppointmentAttendancePutDto;
import rs.ac.bg.fon.FitnessPortal.dtos.appointment.AppointmentWithMemberGetDto;
import rs.ac.bg.fon.FitnessPortal.dtos.appointment.AppointmentWithTrainingGetDto;
import rs.ac.bg.fon.FitnessPortal.exception_handling.InvalidUserException;
import rs.ac.bg.fon.FitnessPortal.services.AppointmentService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("api/v1/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/member/{email}")
    public ResponseEntity<List<AppointmentWithTrainingGetDto>> getByMember(@PathVariable String email, Authentication auth){
        if(!isLoggedInUser(auth, email)) throw new InvalidUserException();

        return ResponseEntity.ok(appointmentService.getByMember(email));
    }

    @PreAuthorize("hasRole('ROLE_COACH')")
    @GetMapping("/training/{trainingID}")
    public ResponseEntity<List<AppointmentWithMemberGetDto>> getByTraining(@PathVariable Integer trainingID, Authentication auth){
        String userEmail = auth.getPrincipal().toString();

        return ResponseEntity.ok(appointmentService.getByTraining(trainingID, userEmail));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{trainingID}")
    public ResponseEntity<Void> scheduleAppointment(@PathVariable Integer trainingID, Authentication auth){
        String userEmail = auth.getPrincipal().toString();

        appointmentService.scheduleAppointment(trainingID, userEmail);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_COACH')")
    @PutMapping("/attendance/{appointmentID}")
    public ResponseEntity<AppointmentWithMemberGetDto> updateAttendance(@PathVariable Integer appointmentID,
                                                                        @RequestBody @Valid AppointmentAttendancePutDto appointmentAttendancePutDto, Authentication auth){
        String userEmail = auth.getPrincipal().toString();

        return ResponseEntity.ok(appointmentService.updateAttendance(appointmentID, appointmentAttendancePutDto, userEmail));
    }

    private boolean isLoggedInUser(Authentication auth, String email) {
        return email != null && email.equals(auth.getPrincipal());
    }

    @Autowired
    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
}
