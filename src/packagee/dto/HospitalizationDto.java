/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee.dto;

import org.json.JSONObject;
import packagee.Hospitalization;

public class HospitalizationDto {
    
    public String serialize(Hospitalization hospitalization) {
        JSONObject json = new JSONObject();
        json.put("id", hospitalization.getId());
        json.put("patientId", hospitalization.getPatient().getId());
        json.put("patientName", hospitalization.getPatient().getFirstname() + " " + hospitalization.getPatient().getLastname());
        json.put("doctorId", hospitalization.getDoctor().getId());
        json.put("doctorName", hospitalization.getDoctor().getFirstname() + " " + hospitalization.getDoctor().getLastname());
        json.put("date", hospitalization.getDate().toString());
        json.put("reason", hospitalization.getReason());
        json.put("roomType", hospitalization.getRoomType().name());
        json.put("observation", hospitalization.getObservations());
        json.put("status", hospitalization.getStatus().name());

        return json.toString();
    }
}
