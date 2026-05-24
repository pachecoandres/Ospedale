/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.repository;

import java.util.List;
import packagee.Hospitalization;
        
public interface HospitalizationRepository {
    
    List<Hospitalization> findAll();
    Hospitalization findById(String id);
    
    void add(Hospitalization hospitalization);
    
    List<Hospitalization> findByPatientId(long patientId);
}
