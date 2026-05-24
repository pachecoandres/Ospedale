package packagee.Factory;

import java.time.LocalDateTime;
import packagee.Appointment;
import packagee.Doctor;
import packagee.Patient;
import packagee.Specialty;
import packagee.repository.AppointmentRepository;

public class AppointmentFactory {

    private AppointmentIdFactory idFactory;

    public AppointmentFactory(AppointmentRepository appointmentRepository) {
        this.idFactory = new AppointmentIdFactory(appointmentRepository);
    }

    public Appointment createAppointment(Patient patient, Doctor doctor, Specialty specialty,
            LocalDateTime datetime, String reason, boolean type) {
        String id = idFactory.generateId(patient.getId());
        return new Appointment(id, patient, doctor, specialty, datetime, reason, type);
    }
}
