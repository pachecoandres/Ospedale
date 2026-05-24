/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.repository;

import java.util.ArrayList;
import java.util.List;
import packagee.Appointment;

/**
 *
 * @author natya
 */
public class ListAppointmentRepository implements AppointmentRepository {
    
    private List<Appointment> appointments;
    
    public ListAppointmentRepository(){
        this.appointments = new ArrayList<>();
    }
    
    @Override
    public List<Appointment> findAll() {
        return appointments;
    }
    
    @Override
    public Appointment findById(String id) {
        for (Appointment a : appointments){
            if(a.getId().equals(id)){
                return a;
            }
        }
        return null;
    }
    
    @Override
    public void add(Appointment appointment) {
        appointments.add(appointment);
    }
    
    @Override
    public List<Appointment> findByPatientId(long patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments){
            if(a.getPatient().getId()== patientId){
                result.add(a);
            }
        }
        return result;
    }
    
    @Override
    public List<Appointment> findByDoctorId(long doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments){
            if(a.getDoctor().getId()== doctorId){
                result.add(a);
            }
        }
        return result;
    }
}
