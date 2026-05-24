/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import packagee.Appointment;
import packagee.AppointmentStatus;
import packagee.Doctor;
import packagee.Factory.AppointmentIdFactory;
import packagee.Patient;
import packagee.Specialty;
import packagee.dto.AppointmentDto;
import packagee.repository.AppointmentRepository;
import packagee.repository.UserRepository;
import packagee.validation.UserValidator;

/**
 *
 * @author natya
 */
public class AppointmentController {
        
    private AppointmentRepository appointmentRepository;
    private UserRepository userRepository;
    private UserValidator validator;
    private AppointmentDto appointmentDto;
    private AppointmentIdFactory appointmentIdFactory;
    
    public AppointmentController(AppointmentRepository appointmentRepository, UserRepository userRepository, AppointmentIdFactory appointmentIdFactory) {
        
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.validator = new UserValidator();
        this.appointmentDto = new AppointmentDto();
        this.appointmentIdFactory = appointmentIdFactory;
    }
    
    public ControllerResponse requestAppointment(String patientIdText, String doctorIdText, 
            String specialtyText, String dateText, String timeText, String reason, String typeText) {
        if (validator.isEmpty(patientIdText) || validator.isEmpty(dateText) || 
            validator.isEmpty(timeText) || validator.isEmpty(reason) || 
            validator.isEmpty(typeText)){
            return new ControllerResponse(400, "Complete todos los campos", "{}");
        }
        
        if (!validator.isValidIdText(patientIdText)) {
            return new ControllerResponse(400, "El ID del paciente no es valido", "{}");
        }
        
        if (!validator.isValidDate(dateText)) {
            return new ControllerResponse(400, "La fecha debe tener formato AAAA-MM-DD", "{}");
        }
        
        if (!timeText.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            return new ControllerResponse(400, "La hora debe tener formato HH:mm", "{}");
        }
        
        String []timeParts = timeText.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        
        if (minute != 0 && minute != 15 && minute != 30 && minute != 45) {
            return new ControllerResponse(400, " Los minutos deben ser 00,15, 30 o 45","{}");
        }
        
        long patientId = Long.parseLong(patientIdText);
        Patient patient = (Patient) userRepository.findById(patientId);
        
        if(patient == null) {
            return new ControllerResponse(404, " El paciente no existe", "{}");
        }
        
        LocalDate date = LocalDate.parse(dateText);
        LocalTime time = LocalTime.of(hour, minute);
        LocalDateTime datetime = LocalDateTime.of(date, time);
        
        Doctor  doctor = null;
        Specialty specialty = null;
        
        if (!validator.isEmpty(doctorIdText)) {
            if (!validator.isValidIdText(doctorIdText)){
                return new ControllerResponse(400, "El ID del doctor no es valido", "{}");
            }
            
            long doctorId = Long.parseLong(doctorIdText);
            doctor = (Doctor) userRepository.findById(doctorId);
            
            if (doctor == null) {
                return new ControllerResponse (404, "El doctor no exite", "{}");
            }
            specialty = doctor.getSpecialty();
            
            if (!doctor.isAvailable(datetime)) {
                return new ControllerResponse (409, "El doctor no tiene disponibilidad en este horario", "{}");
            }
        } else if (!validator.isEmpty(specialtyText)) {
            if (!validator.isValidSpecialty(specialtyText)){
                return new ControllerResponse(400, "Seleccione una especialidad valida", "{}");
            }
            specialty = Specialty.fromDisplayString(specialtyText);
            doctor = findAvailableDoctor(specialty, datetime);
            
            if (doctor == null) {
                return new ControllerResponse(404, " No hay doctores disponibles con esa especialidad en este horario", "{}");
            } 
        } else {
            return new ControllerResponse(400, " Debe especificar un doctor o una especialidad", "{}");
        }
        
        boolean type = typeText.equals("Virtual");
        
        String appointmentId = appointmentIdFactory.generateId(patientId);
        
        Appointment appointment = new Appointment(appointmentId, patient, doctor, specialty, datetime, reason, type);
        
        appointmentRepository.add(appointment);
        return new ControllerResponse(201, "Cita solicitada correctamente", 
                appointmentDto.serialize(appointment));
    }
    
    public ControllerResponse cancelAppointment(String appointmentId) {
        if (validator.isEmpty(appointmentId)) {
            return new ControllerResponse(400, "Ingrese el ID de la cita", "{}");
        }
        
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if (appointment == null) {
            return new ControllerResponse(404, "La cita no existe", "{}");
        }
        
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return new ControllerResponse(400, "No se puede cancelar una cita completada", "{}");
        }
        
        appointment.setStatus(AppointmentStatus.CANCELED);
        return new ControllerResponse(200, "Cita cancelada", appointmentDto.serialize(appointment));
    }
    public ControllerResponse acceptAppointment(String appointmentId) {
        if (validator.isEmpty(appointmentId)) {
            return new ControllerResponse(400, "Ingrese el ID de la cita", "{}");
        }
        
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if (appointment == null) {
            return new ControllerResponse(404, "La cita no existe", "{}");
        }
        
        if (appointment.getStatus() != AppointmentStatus.REQUESTED) {
            return new ControllerResponse(400, "Solo se pueden aceptar citas en estado REQUESTED", "{}");
        }
        
        appointment.setStatus(AppointmentStatus.PENDING);
        return new ControllerResponse(200, "Cita aceptada", appointmentDto.serialize(appointment));
    }
    
    public ControllerResponse completeAppointment(String appointmentId, String diagnosis, 
            String observations, String recommendedTreatment, String followUp) {
        if (validator.isEmpty(appointmentId)) {
            return new ControllerResponse(400, "Ingrese el ID de la cita", "{}");
        }
        
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if (appointment == null) {
            return new ControllerResponse(404, "La cita no existe", "{}");
        }
        
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            return new ControllerResponse(400, "Solo se pueden completar citas en estado PENDING", "{}");
        }
        
        if (!validator.isEmpty(diagnosis)) {
            appointment.setDiagnosis(diagnosis);
        }
        if (!validator.isEmpty(observations)) {
            appointment.setObservations(observations);
        }
        if (!validator.isEmpty(recommendedTreatment)) {
            appointment.setRecommendedTreatment(recommendedTreatment);
        }
        if (!validator.isEmpty(followUp)) {
            appointment.setFollowUp(followUp);
        }
        
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return new ControllerResponse(200, "Cita completada", appointmentDto.serialize(appointment));
    }
    
    public ControllerResponse rescheduleAppointment(String appointmentId, String newTime, String additionalReason) {
        if (validator.isEmpty(appointmentId) || validator.isEmpty(newTime)) {
            return new ControllerResponse(400, "Complete todos los campos", "{}");
        }
        
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if (appointment == null) {
            return new ControllerResponse(404, "La cita no existe", "{}");
        }
        
        if (!newTime.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            return new ControllerResponse(400, "La hora debe tener formato HH:mm (24 horas)", "{}");
        }
        
        String[] timeParts = newTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        
        if (minute != 0 && minute != 15 && minute != 30 && minute != 45) {
            return new ControllerResponse(400, "Los minutos deben ser 00, 15, 30 o 45", "{}");
        }
        
        LocalDateTime currentDatetime = appointment.getDatetime();
        LocalDateTime newDatetime = currentDatetime.withHour(hour).withMinute(minute);
        
        
        Doctor doctor = appointment.getDoctor();
        if (!doctor.isAvailable(newDatetime)) {
            return new ControllerResponse(409, "El doctor no tiene disponibilidad en ese horario", "{}");
        }
        
        appointment.setDatetime(newDatetime);
        
        String currentReason = appointment.getReason();
        if (!validator.isEmpty(additionalReason)) {
            appointment.setReason(currentReason + " | Reagendada: " + additionalReason);
        }
        
        return new ControllerResponse(200, "Cita reagendada correctamente", appointmentDto.serialize(appointment));
    }
    
    private Doctor findAvailableDoctor(Specialty specialty, LocalDateTime datetime) {
        List<packagee.User> allUsers = userRepository.findAll();
        for (packagee.User user : allUsers) {
            if (user instanceof Doctor) {
                Doctor doctor = (Doctor) user;
                if (doctor.getSpecialty() == specialty && doctor.isAvailable(datetime)) {
                    return doctor;
               }
            }
        }
        return null;
    }
}
