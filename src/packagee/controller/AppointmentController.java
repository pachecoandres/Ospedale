package packagee.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import packagee.Appointment;
import packagee.AppointmentStatus;
import packagee.Doctor;
import packagee.Patient;
import packagee.Specialty;
import packagee.User;
import packagee.dto.AppointmentDto;
import packagee.repository.AppointmentRepository;
import packagee.repository.UserRepository;
import packagee.validation.UserValidator;

public class AppointmentController {

    private AppointmentRepository appointmentRepository;
    private UserRepository userRepository;
    private UserValidator validator;
    private AppointmentDto appointmentDto;

    public AppointmentController(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.validator = new UserValidator();
        this.appointmentDto = new AppointmentDto();
    }

    public ControllerResponse requestAppointment(String patientIdText, String doctorOrSpecialty, boolean bySpecialty,
            String dateText, String timeText, String typeText, String reason) {
        if (hasEmptyData(patientIdText, doctorOrSpecialty, dateText, timeText, typeText, reason)) {
            return new ControllerResponse(400, "Complete todos los campos de la cita", "{}");
        }
        if (!validator.isValidIdText(patientIdText)) {
            return new ControllerResponse(400, "El ID del paciente no es valido", "{}");
        }
        if (!validator.isValidDate(dateText)) {
            return new ControllerResponse(400, "La fecha debe tener formato AAAA-MM-DD", "{}");
        }
        if (!validator.isValidTime(timeText)) {
            return new ControllerResponse(400, "La hora debe ser HH:mm con minutos 00, 15, 30 o 45", "{}");
        }
        if (!typeText.equals("Remote") && !typeText.equals("In-person")) {
            return new ControllerResponse(400, "Seleccione un tipo de cita valido", "{}");
        }

        User user = userRepository.findById(Long.parseLong(patientIdText));
        if (!(user instanceof Patient)) {
            return new ControllerResponse(404, "Paciente no encontrado", "{}");
        }

        LocalDateTime datetime = LocalDateTime.of(LocalDate.parse(dateText), LocalTime.parse(timeText));
        Doctor doctor = findDoctor(doctorOrSpecialty, bySpecialty, datetime);
        if (doctor == null) {
            return new ControllerResponse(404, "No se encontro doctor disponible", "{}");
        }

        boolean type = typeText.equals("In-person");
        Patient patient = (Patient) user;
        Appointment appointment = new Appointment(generateId(patient.getId()), patient, doctor, doctor.getSpecialty(), datetime, reason, type);
        appointmentRepository.add(appointment);
        return new ControllerResponse(201, "Cita solicitada correctamente", appointmentDto.serialize(appointment));
    }

    public ControllerResponse cancelAppointment(String appointmentId) {
        if (validator.isEmpty(appointmentId) || appointmentId.equals("Select one")) {
            return new ControllerResponse(400, "Seleccione una cita", "{}");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId);
        if (appointment == null) {
            return new ControllerResponse(404, "La cita no existe", "{}");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return new ControllerResponse(400, "No se puede cancelar una cita completada", "{}");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        return new ControllerResponse(200, "Cita cancelada correctamente", appointmentDto.serialize(appointment));
    }

    public ControllerResponse listPatientAppointments(String patientIdText) {
        if (!validator.isValidIdText(patientIdText)) {
            return new ControllerResponse(400, "El ID del paciente no es valido", "[]");
        }
        return new ControllerResponse(200, "Citas cargadas", appointmentDto.serializeList(appointmentRepository.findByPatientId(Long.parseLong(patientIdText))));
    }

    private Doctor findDoctor(String doctorOrSpecialty, boolean bySpecialty, LocalDateTime datetime) {
        for (User user : userRepository.findAll()) {
            if (user instanceof Doctor) {
                Doctor doctor = (Doctor) user;
                if (matchesDoctor(doctor, doctorOrSpecialty, bySpecialty) && doctor.isAvailable(datetime)) {
                    return doctor;
                }
            }
        }
        return null;
    }

    private boolean matchesDoctor(Doctor doctor, String doctorOrSpecialty, boolean bySpecialty) {
        if (bySpecialty) {
            if (!validator.isValidSpecialty(doctorOrSpecialty)) {
                return false;
            }
            Specialty specialty = Specialty.fromDisplayString(doctorOrSpecialty);
            return doctor.getSpecialty() == specialty;
        }
        if (!validator.isValidIdText(doctorOrSpecialty)) {
            return false;
        }
        return doctor.getId() == Long.parseLong(doctorOrSpecialty);
    }

    private String generateId(long patientId) {
        int count = 1;
        String prefix = "A-" + patientId + "-";
        for (Appointment appointment : appointmentRepository.findAll()) {
            if (appointment.getId().startsWith(prefix)) {
                count++;
            }
        }
        return prefix + String.format("%04d", count);
    }

    private boolean hasEmptyData(String... values) {
        for (String value : values) {
            if (validator.isEmpty(value) || value.equals("Select one")) {
                return true;
            }
        }
        return false;
    }
}
