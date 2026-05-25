/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.Factory;

import java.util.List;
import packagee.Appointment;
import packagee.repository.AppointmentRepository;

public class AppointmentIdFactory {
    
    private AppointmentRepository appointmentRepository;
    
    public AppointmentIdFactory(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    
    public String generateId(long patientId) {
        List<Appointment> patientAppointments = appointmentRepository.findByPatientId(patientId);
        int count = patientAppointments.size();
        return String.format("A-%d-%04d", patientId, count);
    }
}
