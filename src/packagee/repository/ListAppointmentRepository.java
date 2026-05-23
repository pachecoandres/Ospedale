package packagee.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import packagee.Appointment;

public class ListAppointmentRepository implements AppointmentRepository {

    private List<Appointment> appointments;

    public ListAppointmentRepository(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public List<Appointment> findAll() {
        return appointments;
    }

    @Override
    public Appointment findById(String id) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(id)) {
                return appointment;
            }
        }
        return null;
    }

    @Override
    public List<Appointment> findByPatientId(long patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getId() == patientId) {
                result.add(appointment);
            }
        }
        result.sort(Comparator.comparing(Appointment::getDatetime).reversed());
        return result;
    }

    @Override
    public void add(Appointment appointment) {
        appointments.add(appointment);
    }
}
