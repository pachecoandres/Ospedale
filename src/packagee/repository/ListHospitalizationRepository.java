/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.repository;

import java.util.ArrayList;
import java.util.List;
import packagee.Hospitalization;
import packagee.observer.DataObserver;

public class ListHospitalizationRepository implements HospitalizationRepository {
    
    private List<Hospitalization> hospitalizations;
    private List<DataObserver> observers;
    
    public ListHospitalizationRepository(){
        this.hospitalizations = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public ListHospitalizationRepository(List<Hospitalization> hospitalizations){
        this.hospitalizations = hospitalizations;
        this.observers = new ArrayList<>();
    }
    
    @Override
    public List<Hospitalization> findAll() {
        return hospitalizations;
    }
    
    @Override
    public Hospitalization findById(String id) {
        for (Hospitalization h : hospitalizations){
            if(h.getId().equals(id)){
                return h;
            }
        }
        return null;
    }
    
    @Override
    public void add(Hospitalization hospitalization) {
        hospitalizations.add(hospitalization);
        notifyObservers();
    }
    
    @Override
    public List<Hospitalization> findByPatientId(long patientId) {
        List<Hospitalization> result = new ArrayList<>();
        for (Hospitalization h : hospitalizations){
            if(h.getPatient().getId()== patientId){
                result.add(h);
            }
        }
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
