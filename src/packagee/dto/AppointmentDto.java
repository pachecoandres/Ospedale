/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.dto;

import org.json.JSONArray;
import org.json.JSONObject;
import packagee.Appointment;
import packagee.Prescription;

/**
 *
 * @author natya
 */
public class AppointmentDto {
    
    public String serialize(Appointment appointment) {
        JSONObject json = new JSONObject();
        
        json.put("id", appointment.getId());
        json.put("patientId", appointment.getPatient().getId());
        json.put("patientName", appointment.getPatient().getFirstname() + " " + appointment.getPatient().getLastname());
        json.put("doctorId", appointment.getDoctor().getId());
        json.put("doctorName", appointment.getDoctor().getFirstname() + " " + appointment.getDoctor().getLastname());
        json.put("specialty", appointment.getSpecialty().name());
        json.put("datetime", appointment.getDatetime().toString());
        json.put("reason", appointment.getReason());
        json.put("type", appointment.isType() ? "In-person" : "Remote");
        json.put("status", appointment.getStatus().name());
        
        if (appointment.getDiagnosis() != null) {
            json.put("diagnosis", appointment.getDiagnosis());
        }
        
        if (appointment.getObservations() != null) {
            json.put("observations", appointment.getObservations());
        }
        
        if (appointment.getRecommendedTreatment() != null) {
            json.put("recommendedTreatment", appointment.getRecommendedTreatment());
        }
        
        if (appointment.getFollowUp() != null) {
            json.put("followUp", appointment.getFollowUp());
        }
        
        JSONArray prescriptions = new JSONArray();
        for (Prescription p : appointment.getPrescriptions()) {
            JSONObject prescJson = new JSONObject();
            prescJson.put("medicationName", p.getMedicationName());
            prescJson.put("dose", p.getDose());
            prescJson.put("frequency", p.getFrequency());
            prescriptions.put(prescJson);
            
        }
        json.put("prescriptions", prescriptions);
        
        return json.toString();
    }

    public String serializeList(java.util.List<Appointment> appointments) {
        JSONArray array = new JSONArray();
        for (Appointment appointment : appointments) {
            array.put(new JSONObject(serialize(appointment)));
        }
        return array.toString();
    }
    
}
