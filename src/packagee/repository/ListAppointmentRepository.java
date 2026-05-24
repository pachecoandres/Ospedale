/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import packagee.Appointment;
import packagee.observer.DataObserver;

/**
 *
 * @author natya
 */
public class ListAppointmentRepository implements AppointmentRepository {
    
    private List<Appointment> appointments;
    private List<DataObserver> observers;
    
    public ListAppointmentRepository(){
        this.appointments = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public ListAppointmentRepository(List<Appointment> appointments){
        this.appointments = appointments;
        this.observers = new ArrayList<>();
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
        notifyObservers();
    }
    
    @Override
    public List<Appointment> findByPatientId(long patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments){
            if(a.getPatient().getId()== patientId){
                result.add(a);
            }
        }
        result.sort(Comparator.comparing(Appointment::getDatetime).reversed());
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
        result.sort(Comparator.comparing(Appointment::getDatetime).reversed());
        return result;
    }

    @Override
    public void addObserver(DataObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (DataObserver observer : observers) {
            observer.update();
        }
    }
}
