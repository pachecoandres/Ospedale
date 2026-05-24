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
import packagee.Factory.AppointmentFactory;
import packagee.Factory.AppointmentIdFactory;
import packagee.Patient;
import packagee.Specialty;
import packagee.dto.AppointmentDto;
import packagee.repository.AppointmentRepository;
import packagee.repository.UserRepository;
import packagee.strategy.DoctorByIdSearchStrategy;
import packagee.strategy.DoctorBySpecialtySearchStrategy;
import packagee.strategy.DoctorSearchStrategy;
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
    private AppointmentFactory appointmentFactory;
    
    public AppointmentController(AppointmentRepository appointmentRepository, UserRepository userRepository, AppointmentIdFactory appointmentIdFactory) {
        
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.validator = new UserValidator();
        this.appointmentDto = new AppointmentDto();
        this.appointmentIdFactory = appointmentIdFactory;
        this.appointmentFactory = new AppointmentFactory(appointmentRepository);
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
        
        if (!validator.isValidTime(timeText)) {
            return new ControllerResponse(400, "La hora debe tener formato HH:mm con minutos 00, 15, 30 o 45", "{}");
        }
        
        String []timeParts = timeText.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        
        long patientId = Long.parseLong(patientIdText);
        packagee.User patientUser = userRepository.findById(patientId);
        
        if(!(patientUser instanceof Patient)) {
            return new ControllerResponse(404, " El paciente no existe", "{}");
        }
        Patient patient = (Patient) patientUser;
        
        LocalDate date = LocalDate.parse(dateText);
        LocalTime time = LocalTime.of(hour, minute);
        LocalDateTime datetime = LocalDateTime.of(date, time);
        
        Doctor  doctor = null;
        Specialty specialty = null;
        
        if (!validator.isEmpty(doctorIdText)) {
            if (!validator.isValidIdText(doctorIdText)){
                return new ControllerResponse(400, "El ID del doctor no es valido", "{}");
            }
            
            doctor = searchDoctor(new DoctorByIdSearchStrategy(), doctorIdText, datetime);
            if (doctor == null) {
                return new ControllerResponse (404, "El doctor no exite", "{}");
            }
            specialty = doctor.getSpecialty();
        } else if (!validator.isEmpty(specialtyText)) {
            if (!validator.isValidSpecialty(specialtyText)){
                return new ControllerResponse(400, "Seleccione una especialidad valida", "{}");
            }
            specialty = Specialty.fromDisplayString(specialtyText);
            doctor = searchDoctor(new DoctorBySpecialtySearchStrategy(), specialtyText, datetime);
            
            if (doctor == null) {
                return new ControllerResponse(404, " No hay doctores disponibles con esa especialidad en este horario", "{}");
            } 
        } else {
            return new ControllerResponse(400, " Debe especificar un doctor o una especialidad", "{}");
        }
        
        boolean type = typeText.equals("In-person") || typeText.equals("Presencial");
        
        Appointment appointment = appointmentFactory.createAppointment(patient, doctor, specialty, datetime, reason, type);
        
        appointmentRepository.add(appointment);
        return new ControllerResponse(201, "Cita solicitada correctamente", 
                appointmentDto.serialize(appointment));
    }

    public ControllerResponse listPatientAppointments(String patientIdText) {
        if (!validator.isValidIdText(patientIdText)) {
            return new ControllerResponse(400, "El ID del paciente no es valido", "[]");
        }
        return new ControllerResponse(200, "Citas cargadas", appointmentDto.serializeList(appointmentRepository.findByPatientId(Long.parseLong(patientIdText))));
    }

    public ControllerResponse listDoctorAppointments(String doctorIdText) {
        if (!validator.isValidIdText(doctorIdText)) {
            return new ControllerResponse(400, "El ID del doctor no es valido", "[]");
        }
        return new ControllerResponse(200, "Citas cargadas", appointmentDto.serializeList(appointmentRepository.findByDoctorId(Long.parseLong(doctorIdText))));
    }
    
    public ControllerResponse cancelAppointment(String appointmentId) {
        if (validator.isEmpty(appointmentId) || appointmentId.equals("Select one")) {
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
        appointmentRepository.notifyObservers();
        return new ControllerResponse(200, "Cita cancelada", appointmentDto.serialize(appointment));
    }
    public ControllerResponse acceptAppointment(String appointmentId) {
        if (validator.isEmpty(appointmentId) || appointmentId.equals("Select one")) {
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
        appointmentRepository.notifyObservers();
        return new ControllerResponse(200, "Cita aceptada", appointmentDto.serialize(appointment));
    }
    
    public ControllerResponse completeAppointment(String appointmentId, String diagnosis, 
            String observations, String recommendedTreatment, String followUp) {
        if (validator.isEmpty(appointmentId) || appointmentId.equals("Select one")) {
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
        appointmentRepository.notifyObservers();
        return new ControllerResponse(200, "Cita completada", appointmentDto.serialize(appointment));
    }
    
    public ControllerResponse rescheduleAppointment(String appointmentId, String newTime, String additionalReason) {
        if (validator.isEmpty(appointmentId) || appointmentId.equals("Select one") || validator.isEmpty(newTime)) {
            return new ControllerResponse(400, "Complete todos los campos", "{}");
        }
        
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if (appointment == null) {
            return new ControllerResponse(404, "La cita no existe", "{}");
        }
        
        if (!validator.isValidTime(newTime)) {
            return new ControllerResponse(400, "La hora debe tener formato HH:mm con minutos 00, 15, 30 o 45", "{}");
        }
        
        String[] timeParts = newTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        
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
        
        appointmentRepository.notifyObservers();
        return new ControllerResponse(200, "Cita reagendada correctamente", appointmentDto.serialize(appointment));
    }
    
    private Doctor searchDoctor(DoctorSearchStrategy strategy, String value, LocalDateTime datetime) {
        return strategy.findDoctor(userRepository, value, datetime);
    }
}
