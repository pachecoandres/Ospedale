/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.controller;

import java.time.LocalDate;
import java.util.List;
import packagee.Appointment;
import packagee.AppointmentStatus;
import packagee.Doctor;
import packagee.Hospitalization;
import packagee.HospitalizationStatus;
import packagee.Patient;
import packagee.RoomType;
import packagee.dto.HospitalizationDto;
import packagee.repository.HospitalizationRepository;
import packagee.repository.UserRepository;
import packagee.validation.UserValidator;


public class HospitalizationController {
    
    private HospitalizationRepository hospitalizationRepository;
    private UserRepository userRepository;
    private UserValidator validator;
    private HospitalizationDto hospitalizationDto;
    
    public HospitalizationController(HospitalizationRepository hospitalizationRepository, UserRepository userRepository) {
        this.hospitalizationRepository = hospitalizationRepository;
        this.userRepository = userRepository;
        this.validator = new UserValidator();
        this.hospitalizationDto = new HospitalizationDto();
    }
    
    public ControllerResponse requestHospitalization(String patientIdText, String doctorIdText, 
            String dateText, String reason, String roomTypeText, String observations) {
        if (validator.isEmpty(patientIdText) || validator.isEmpty(doctorIdText) || 
            validator.isEmpty(dateText) || validator.isEmpty(reason) || 
            validator.isEmpty(roomTypeText) || validator.isEmpty(observations)) {
            return new ControllerResponse(400, "Complete todos los campos", "{}");
        }
        
        if (!validator.isValidIdText(patientIdText)) {
            return new ControllerResponse(400, "El ID del paciente no es valido", "{}");
        }
        if (!validator.isValidIdText(doctorIdText)) {
            return new ControllerResponse(400, "El ID del doctor no es valido", "{}");
        }
        if (!validator.isValidDate(dateText)) {
            return new ControllerResponse(400, "La fecha debe tener formato AAAA-MM-DD", "{}");
        }
        
        long patientId = Long.parseLong(patientIdText);
        long doctorId = Long.parseLong(doctorIdText);
        
        Patient patient = (Patient) userRepository.findById(patientId);
        if (patient == null) {
            return new ControllerResponse(404, "El paciente no existe", "{}");
        }
        
        Doctor doctor = (Doctor) userRepository.findById(doctorId);
        if (doctor == null) {
            return new ControllerResponse(404, "El doctor no existe", "{}");
        }
        
        RoomType roomType;
        try {
            roomType = RoomType.valueOf(roomTypeText);
        } catch (Exception e) {
            return new ControllerResponse(400, "Tipo de habitacion no valido", "{}");
        }
        
        String hospitalizationId = generateHospitalizationId(patientId);
        LocalDate date = LocalDate.parse(dateText);
        
        Hospitalization hospitalization = new Hospitalization(hospitalizationId, patient, doctor, 
                date, reason, roomType, observations);
        
        hospitalizationRepository.add(hospitalization);
        return new ControllerResponse(201, "Hospitalizacion solicitada correctamente", 
                hospitalizationDto.serialize(hospitalization));
    }
    
    public ControllerResponse approveHospitalization(String hospitalizationId) {
        if (validator.isEmpty(hospitalizationId)) {
            return new ControllerResponse(400, "Ingrese el ID de la hospitalizacion", "{}");
        }
        
        Hospitalization hospitalization = hospitalizationRepository.findById(hospitalizationId);
        if (hospitalization == null) {
            return new ControllerResponse(404, "La hospitalizacion no existe", "{}");
        }
        
        hospitalization.setStatus(HospitalizationStatus.ONGOING);
        return new ControllerResponse(200, "Hospitalizacion aprobada", 
                hospitalizationDto.serialize(hospitalization));
    }
    
    public ControllerResponse cancelHospitalization(String hospitalizationId) {
        if (validator.isEmpty(hospitalizationId)) {
            return new ControllerResponse(400, "Ingrese el ID de la hospitalizacion", "{}");
        }
        
        Hospitalization hospitalization = hospitalizationRepository.findById(hospitalizationId);
        if (hospitalization == null) {
            return new ControllerResponse(404, "La hospitalizacion no existe", "{}");
        }
        
        hospitalization.setStatus(HospitalizationStatus.CANCELED);
        return new ControllerResponse(200, "Hospitalizacion cancelada", 
                hospitalizationDto.serialize(hospitalization));
    }
    
    public ControllerResponse hospitalizeFromAppointment(Appointment appointment, String reason, 
            String roomTypeText, String observations) {
        if (appointment == null) {
            return new ControllerResponse(400, "La cita no existe", "{}");
        }
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            return new ControllerResponse(400, "La cita debe estar en estado PENDING", "{}");
        }
        if (validator.isEmpty(reason) || validator.isEmpty(roomTypeText) || validator.isEmpty(observations)) {
            return new ControllerResponse(400, "Complete todos los campos", "{}");
        }
        
        RoomType roomType;
        try {
            roomType = RoomType.valueOf(roomTypeText);
        } catch (Exception e) {
            return new ControllerResponse(400, "Tipo de habitacion no valido", "{}");
        }
        
        appointment.setStatus(AppointmentStatus.COMPLETED);
        
        String hospitalizationId = generateHospitalizationId(appointment.getPatient().getId());
        LocalDate date = LocalDate.now();
        
        Hospitalization hospitalization = new Hospitalization(hospitalizationId, 
                appointment.getPatient(), appointment.getDoctor(), date, reason, roomType, 
                observations, HospitalizationStatus.ONGOING);
        
        hospitalizationRepository.add(hospitalization);
        return new ControllerResponse(201, "Paciente hospitalizado desde cita", 
                hospitalizationDto.serialize(hospitalization));
    }
    
    private String generateHospitalizationId(long patientId) {
        List<Hospitalization> patientHospitalizations = hospitalizationRepository.findByPatientId(patientId);
        int count = patientHospitalizations.size();
        return String.format("H-%d-%04d", patientId, count);
    }
}
