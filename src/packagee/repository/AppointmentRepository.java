package packagee.repository;

import java.util.List;
import packagee.Appointment;

public interface AppointmentRepository {

    List<Appointment> findAll();

    Appointment findById(String id);

    List<Appointment> findByPatientId(long patientId);

    void add(Appointment appointment);
}
