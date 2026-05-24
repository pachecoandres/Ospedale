/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.dto;

import org.json.JSONObject;
import packagee.Prescription;

/**
 *
 * @author natya
 */
public class PrescriptionDto {
    
    public String serialize(Prescription prescription) {
        JSONObject json = new JSONObject();
        json.put("medicationName", prescription.getMedicationName());
        json.put("dose", prescription.getDose());
        json.put("administrationRoute", prescription.getAdministrationRoute());
        json.put("treatmentDuration", prescription.getTreatmentDuration());
        json.put("frequency", prescription.getFrequency());
        json.put("additionalInstructions", prescription.getAdditionalInstructions());
        return json.toString();
    
    }
}
