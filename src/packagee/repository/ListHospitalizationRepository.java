/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.repository;

import java.util.ArrayList;
import java.util.List;
import packagee.Hospitalization;

public class ListHospitalizationRepository implements HospitalizationRepository {
    
    private List<Hospitalization> hospitalizations;
    
    public ListHospitalizationRepository(){
        this.hospitalizations = new ArrayList<>();
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
    
}
