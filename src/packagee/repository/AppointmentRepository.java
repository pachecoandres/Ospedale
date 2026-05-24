/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.repository;

import java.util.List;
import packagee.Appointment;
import packagee.observer.DataObserver;

public interface AppointmentRepository {
    
    List<Appointment> findAll();
    Appointment findById(String id);
    
    void add(Appointment appointment);
        
    List<Appointment> findByPatientId(long patientId);
    List<Appointment> findByDoctorId(long doctorId);

    void addObserver(DataObserver observer);

    void notifyObservers();
    
}
